# Alfresco Hello World Transformer Examples

## Purpose

This repository contains two example Hello World Transformers. It is used in [migrating-a-legacy-transformer](https://github.com/Alfresco/acs-packaging/blob/master/docs/migrating-a-legacy-transformer.md) (in acs-packaging) to guide a developer through the process of taking an existing custom transformer and building a new one that will be compatible with future versions.

In Alfresco Content Service 6.0.0, the Enterprise edition allowed transforms to run in separate Docker containers, but they could still run within the content repository. With the deployment to Docker, the use of AMPs to extend Alfresco is being discouraged. There is also a need for both Enterprise and Community editions to share a common transform mechanism.
With the release of Alfresco Content Service 6.2.0 both Community and Enterprise editions may use transforms in Docker containers through a LocalTransformService. It replaces the older ContentService transformers (now named Legacy transforms) which may be removed in a future release.
The new LocalTransformService takes advantages of moving the Transformation to a different process (docker container) bringing all of the advantages of using docker containers and a much simpler way of developing and configuring a custom Transformer.

* [Alfresco Hello-World Transformer AMP](alfresco-helloworld-transformer-amp/).
* [Alfresco Hello-World Transformer T-engine](alfresco-helloworld-transformer-engine/).