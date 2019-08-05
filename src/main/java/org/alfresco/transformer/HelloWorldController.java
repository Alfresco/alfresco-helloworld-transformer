/*
 * #%L
 * Alfresco Transform Core
 * %%
 * Copyright (C) 2005 - 2019 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software.
 * -
 * If the software was purchased under a paid Alfresco license, the terms of
 * the paid license agreement will prevail.  Otherwise, the software is
 * provided under the following open source license terms:
 * -
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * -
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.transformer;

import org.alfresco.transform.exceptions.TransformException;
import org.alfresco.transformer.logging.LogEntry;
import org.alfresco.transformer.probes.ProbeTestTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.alfresco.transformer.fs.FileManager.createAttachment;
import static org.alfresco.transformer.fs.FileManager.createSourceFile;
import static org.alfresco.transformer.fs.FileManager.createTargetFile;
import static org.alfresco.transformer.fs.FileManager.createTargetFileName;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * Controller for handling requests to the Hello World T-Engine. The T-Engine takes an input text file
 * containing a single name and a language parameter and transforms it into a HTML file.
 * For an input file containing the name "Tom" and language "English" this T-Engine will return a HTML file
 * with body: "Hello World! Hello Tom!".
 * This example can say hello in 3 languages: English, Spanish and German.
 *
 */
@Controller
public class HelloWorldController extends AbstractTransformerController
{
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    private static final String HTML_TEMPLATE = "<!DOCTYPE html><html><head><meta charset=\"utf-8\">" +
            "<title>Hello World</title></head><body><h1 style=\"color:blue\">T-Engine Example</h1><p>%s</p></body></html>";

    private static final Map<String, String> HW_DICTIONARY = new HashMap<>();

    public HelloWorldController()
    {
        logger.info("-------------------------------------------" );
        logger.info( getTransformerName() + " is starting up" );
        logger.info("-------------------------------------------" );

        HW_DICTIONARY.put("english", "Hello World! Hello %s!");
        HW_DICTIONARY.put("spanish", "¡Hola Mundo! ¡Hola %s!");
        HW_DICTIONARY.put("german",  "Hallo Welt! Hallo %s! ");
    }

    /**
     * This endpoint is called by ACS (Alfresco Content Repository) when a supported transformation is requested.
     *
     * @param request The original request
     * @param sourceMultipartFile ACS will always provide the source file
     * @param targetExtension ACS will always provide the target extension
     * @param language ACS will provide any additional parameters defined as options in resources/engine_config.json
     * @return response body including the result of the transformation
     */
    @PostMapping(value = "/transform", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> transform(HttpServletRequest request,
                                              @RequestParam("file") MultipartFile sourceMultipartFile,
                                              @RequestParam(value = "targetExtension") String targetExtension,
                                              @RequestParam(value = "language") String language)
    {

        logger.info("Performing transformation using " + getTransformerName() + ". Language="+language );

        // Prepare source and target files
        String targetFilename = createTargetFileName(sourceMultipartFile.getOriginalFilename(), targetExtension);
        getProbeTestTransform().incrementTransformerCount();
        File sourceFile = createSourceFile(request, sourceMultipartFile);
        File targetFile = createTargetFile(request, targetFilename);

        // Do the transformation
        transformInternal(sourceFile, targetFile, language);

        // Prepare the response
        final ResponseEntity<Resource> body = createAttachment(targetFilename, targetFile);
        LogEntry.setTargetSize(targetFile.length());
        long time = LogEntry.setStatusCodeAndMessage(OK.value(), "Success");
        getProbeTestTransform().recordTransformTime(time);
        return body;
    }

    /**
     * This method is processes transform requests from a message queue,
     * it performs the same transform as {@link #transform(HttpServletRequest, MultipartFile, String, String)}
     * @param sourceFile The source file
     * @param targetFile The target file
     * @param transformOptions transformOptions such as the ones defined in resources/engine_config.json
     * @param timeout The requested transform timeout value
     */
    @Override
    public void processTransform(File sourceFile, File targetFile, Map<String, String> transformOptions, Long timeout)
    {
        String language = transformOptions.get("language");
        transformInternal(sourceFile, targetFile, language);
    }

    /**
     *
     * Simple transform text -> html
     * @see <a href="https://github.com/Alfresco/alfresco-transform-core/blob/master/docs/Probes.md">Probes.md</a>
     * @return A quick transform used to check the health of the T-Engine
     */
    @Override
    public ProbeTestTransform getProbeTestTransform()
    {
        //
        return new ProbeTestTransform(this, "probe_test.txt", "probe_test.html",
                180, 20, 150, 1024,
                1,1)
        {
            @Override
            protected void executeTransformCommand(File sourceFile, File targetFile)
            {
                transformInternal(sourceFile, targetFile, "Spanish");
            }
        };
    }

    @Override
    public String getTransformerName()
    {
        return "Alfresco Hello World Transformer";
    }

    @Override
    public String version()
    {
        return getTransformerName() + " available";
    }

    /**
     *
     * The actual transformation code.
     *
     * @param sourceFile - The received source file
     * @param targetFile - The target file representing the result of the transformation
     * @param language - The language to say hello in
     */
    private void transformInternal(File sourceFile, File targetFile, String language)
    {
        String greeting = HW_DICTIONARY.get(language.toLowerCase());

        if (greeting == null)
        {
            throw new TransformException(BAD_REQUEST.value(),
                    "We don't have a greeting for input language '" + language + "'." );
        }

        try
        {
            String nameFromFile = Files.readString(sourceFile.toPath());
            String bodyContent = String.format(greeting, nameFromFile);
            String html = String.format(HTML_TEMPLATE, bodyContent);
            Files.writeString(targetFile.toPath(), html);
        }
        catch (Exception e)
        {
            throw new TransformException(INTERNAL_SERVER_ERROR.value(),
                    "There was a problem during transformation: " + e.getMessage() );
        }
    }
}
