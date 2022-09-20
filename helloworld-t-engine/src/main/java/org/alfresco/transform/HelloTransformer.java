/*
 * #%L
 * Alfresco Transform Core
 * %%
 * Copyright (C) 2022 - 2022 Alfresco Software Limited
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
package org.alfresco.transform;

import org.alfresco.transform.base.CustomTransformer;
import org.alfresco.transform.base.TransformManager;
import org.alfresco.transform.exceptions.TransformException;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class HelloTransformer implements CustomTransformer
{
    private static final String HTML_TEMPLATE = "<!DOCTYPE html><html><head><meta charset=\"utf-8\">" +
        "<title>Hello World</title></head><body><h1 style=\"color:blue\">T-Engine Example</h1><p>%s</p></body></html>";

    private static final Map<String, String> HW_DICTIONARY = new HashMap<>();
    static
    {
        HW_DICTIONARY.put("english", "Hello World! Hello %s!");
        HW_DICTIONARY.put("spanish", "¡Hola Mundo! ¡Hola %s!");
        HW_DICTIONARY.put("german",  "Hallo Welt! Hallo %s! ");
    }

    @Override
    public String getTransformerName()
    {
        return "helloWorld";
    }

    @Override
    public void transform(String sourceMimetype, InputStream inputStream, String targetMimetype,
        OutputStream outputStream, Map<String, String> transformOptions, TransformManager transformManager)
        throws Exception
    {
        String language = transformOptions.get("language");
        String nameFromFile = new String(inputStream.readAllBytes());

        // Deliberately not doing the NPE check on language, so it is possible to see how that is logged
        String greeting = HW_DICTIONARY.get(language.toLowerCase());

        if (greeting == null)
        {
            throw new IllegalArgumentException("We don't have a greeting for input language '" + language + "'." );
        }
        String bodyContent = String.format(greeting, nameFromFile);
        String html = String.format(HTML_TEMPLATE, bodyContent);

        byte[] bytes = html.getBytes();
        outputStream.write(bytes, 0, bytes.length);
    }
}