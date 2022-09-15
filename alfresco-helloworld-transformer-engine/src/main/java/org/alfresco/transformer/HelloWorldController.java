/*
 * #%L
 * Alfresco Transform Core
 * %%
 * Copyright (C) 2005 - 2020 Alfresco Software Limited
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
import org.alfresco.transformer.probes.ProbeTestTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @deprecated use the newer helloworld-t-engine example if possible.
 *
 * Controller for handling requests to the Hello World T-Engine. The T-Engine takes an input text file
 * containing a single name and a language parameter and transforms it into a HTML file.
 * For an input file containing the name "Tom" and language "English" this T-Engine will return a HTML file
 * with body: "Hello World! Hello Tom!".
 * This example can say hello in 3 languages: English, Spanish and German.
 */
@Deprecated
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
                Map<String, String> transformOptions = Collections.singletonMap("language", "Spanish");
                transformImpl("helloWorld", "text/plain", "text/html", transformOptions, sourceFile, targetFile);
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
     * The actual transformation code.
     *
     * @param transformName - will always be {@code "helloWorld"} as there is only one transformer defined in the
     *                        {@code engine_config.json}.
     * @param sourceMimetype - the media type of the source {@code "text/plain"}
     * @param targetMimetype - the media type to be generated {@code "text/html"}
     * @param transformOptions - options that have been supplied to the transformer
     * @param sourceFile - The received source file
     * @param targetFile - The target file representing the result of the transformation
     */
    @Override
    public void transformImpl(String transformName, String sourceMimetype, String targetMimetype, Map<String, String> transformOptions, File sourceFile, File targetFile)
    {
        String language = transformOptions.get("language");
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
