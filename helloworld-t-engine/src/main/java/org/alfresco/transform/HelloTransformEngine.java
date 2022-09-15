/*
 * #%L
 * Alfresco Transform Core
 * %%
 * Copyright (C) 2005 - 2022 Alfresco Software Limited
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

import com.google.common.collect.ImmutableMap;
import org.alfresco.transform.base.TransformEngine;
import org.alfresco.transform.base.probes.ProbeTransform;
import org.alfresco.transform.config.reader.TransformConfigResourceReader;
import org.alfresco.transform.config.TransformConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Hello World T-Engine contains just one CustomTransformer called {@code "hello"}. It takes an input text file
 * containing a name and a language as a transform option, which it transforms it into a HTML file.
 * For an input file containing the name {@code "Tom"} and language {@code "English"} it will return an HTML file
 * with body: {@code "Hello World! Hello Tom!"}. This example can say hello in 3 languages: English, Spanish and German.
 */
@Component
public class HelloTransformEngine implements TransformEngine
{
    @Autowired
    private TransformConfigResourceReader transformConfigResourceReader;

    @Override
    public String getTransformEngineName()
    {
        // The numeric prefix ensures config from t-engines is always read in the same order if there are multiple
        // engines available to the t-router, as engine names and configuration files are sorted first.
        return "0200 Hello World";
    }

    @Override
    public String getStartupMessage()
    {
        return "Startup "+getTransformEngineName()+"\nNo 3rd party licenses";
    }

    @Override
    public TransformConfig getTransformConfig()
    {
        return transformConfigResourceReader.read("classpath:engine_config.json");
    }

    @Override
    public ProbeTransform getProbeTransform()
    {
        return new ProbeTransform("probe_test.txt", "text/plain", "text/html",
            ImmutableMap.of("language", "Spanish"),
            185, 20, 150, 1024, 1, 1);
    }
}