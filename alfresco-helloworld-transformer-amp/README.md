
# Alfresco Hello World Transformer AMP  
  
## NOTE
This is not an example on how to build a Custom Transformer.

## Purpose
This is an example Hello World transformer AMP used to demonstrate the old mechanism of adding custom Transformers or Renditions to alfresco-content-repository. However this is a AMP is bare-bones, it has no unit tests and it brings the minimum required dependencies.

## AMP Architecture
```
project
│   README.md
│   pom.xml
│   .gitignore
│
└───alfresco-helloworld-transformer-amp
│   │   pom.xml
│   │
│   └───src
│       └───main
|       |     └───amp
|       |         |   module.properties
|       |         |
|       |         └───config.alfresco.module.helloworld-amp
|       |             |   module-context.xml
|       |             |
|       |             └───context
|       |                 |   hello-world-context.xml
|       | 
│       └───java.org.alfresco
|           └───content.transform
|           |   |   HelloWorldTransformer
|           |   |   HelloWorldTransformerOptions
│           │
|           └───repo.rendition2
|               |   HelloWorldOptionConverter
│   
└───packaging
    │   pom.xml
    │   Dockerfile
    |   docker-compose.yml
    |   .maven-dockerignore
```