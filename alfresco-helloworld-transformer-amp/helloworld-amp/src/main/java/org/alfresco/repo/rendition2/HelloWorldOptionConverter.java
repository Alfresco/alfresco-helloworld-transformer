package org.alfresco.repo.rendition2;

import org.alfresco.content.transform.HelloWorldTransformerOptions;
import org.alfresco.service.cmr.repository.TransformationOptions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.util.CollectionUtils.containsAny;

public class HelloWorldOptionConverter extends TransformationOptionsConverter
{
        private static final String LANGUAGE = "language";

        private static Set<String> HELLOWORLD_OPTIONS = new HashSet<>(Arrays.asList(new String[]
                {
                        LANGUAGE
                }));

        @Override
        TransformationOptions getTransformationOptions(String renditionName, Map<String, String> options)
        {
                TransformationOptions transformationOptions = null;

                Set<String> optionNames = options.keySet();

                Set<String> subclassOptionNames = new HashSet<>(optionNames);
                boolean isHelloWorldRendition = containsAny(subclassOptionNames, HELLOWORLD_OPTIONS);

                if(isHelloWorldRendition)
                {
                        HelloWorldTransformerOptions opts = new HelloWorldTransformerOptions();
                        opts.setLanguage(options.get(LANGUAGE));
                        transformationOptions = opts;
                }

                return transformationOptions;
        }
}
