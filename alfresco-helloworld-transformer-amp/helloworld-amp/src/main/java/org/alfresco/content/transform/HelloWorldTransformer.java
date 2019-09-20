package org.alfresco.content.transform;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.transform.AbstractContentTransformer2;
import org.alfresco.repo.content.transform.HtmlParserContentTransformer;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTransformer extends AbstractContentTransformer2
{
        @SuppressWarnings("unused")
        private static final Log logger = LogFactory.getLog(HelloWorldTransformer.class);
        private static final Map<String, String> HW_DICTIONARY = new HashMap<>();
        private static final String HTML_TEMPLATE = "<!DOCTYPE html><html><head><meta charset=\"utf-8\">" +
                "<title>Hello World</title></head><body><h1 style=\"color:blue\">T-Engine Example</h1><p>%s</p></body></html>";

        public HelloWorldTransformer()
        {
                logger.info("-------------------------------------------" );
                logger.info( "Alfresco Hello World Transformer is starting up" );
                logger.info("-------------------------------------------" );

                HW_DICTIONARY.put("english", "Hello World! Hello %s!");
                HW_DICTIONARY.put("spanish", "¡Hola Mundo! ¡Hola %s!");
                HW_DICTIONARY.put("german",  "Hallo Welt! Hallo %s! ");
        }

        @Override protected void transformInternal(ContentReader contentReader, ContentWriter contentWriter,
                TransformationOptions transformationOptions) throws Exception
        {
                HelloWorldTransformerOptions language = ((HelloWorldTransformerOptions) (transformationOptions));
                String greeting = HW_DICTIONARY.get(language.getLanguage().toLowerCase());

                if (greeting == null)
                {
                        throw new AlfrescoRuntimeException("We don't have a greeting for input language '" + language.getLanguage() + "'.");
                }

                String nameFromFile = contentReader.getContentUrl();
                String bodyContent = String.format(greeting, nameFromFile);
                String html = String.format(HTML_TEMPLATE, bodyContent);
                contentWriter.putContent(html);
        }

        /**
         * Only support TEXT to HTML.
         */
        @Override
        public boolean isTransformableMimetype(String sourceMimetype, String targetMimetype, TransformationOptions options)
        {
                boolean isSourceMimetypeText = MimetypeMap.MIMETYPE_TEXT_PLAIN.equals(sourceMimetype);
                boolean isTargetMimetypeHtml = MimetypeMap.MIMETYPE_HTML.equals(targetMimetype);
                return (isSourceMimetypeText && isTargetMimetypeHtml);
        }

        @Override
        public String getComments(boolean available)
        {
                return onlySupports(MimetypeMap.MIMETYPE_TEXT_PLAIN, MimetypeMap.MIMETYPE_HTML, available);
        }
}
