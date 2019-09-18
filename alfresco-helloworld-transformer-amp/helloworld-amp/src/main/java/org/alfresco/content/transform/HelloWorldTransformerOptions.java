package org.alfresco.content.transform;

import org.alfresco.service.cmr.repository.TransformationOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HelloWorldTransformerOptions extends TransformationOptions
{
        private static final String LANGUAGE_OPTION = "language";

        private String language = "german";

        String getLanguage()
        {
                return language;
        }

        public void setLanguage(String language)
        {
                this.language = language;
        }

        @Override
        public Map<String, Object> toMap()
        {
                Map<String, Object> baseProps = super.toMap();
                Map<String, Object> props = new HashMap<>(baseProps);
                props.put(LANGUAGE_OPTION, language);
                return props;
        }

        @Override public boolean equals(Object o)
        {
                if (this == o)
                        return true;
                if (o == null || getClass() != o.getClass())
                        return false;
                if (!super.equals(o))
                        return false;
                HelloWorldTransformerOptions that = (HelloWorldTransformerOptions) o;
                return Objects.equals(language, that.language);
        }

        @Override public int hashCode()
        {
                return Objects.hash(super.hashCode(), language);
        }
}
