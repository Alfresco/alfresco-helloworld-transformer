# Alfresco Hello World Transformer Examples

## Purpose
Alfresco-content-repository 6.0.0 brought the new Docker deployment method and with it a part of the Transformers were moved to separate Docker containers for the enterprise but left in the same docker container for community. With this new deployment method, the use of AMPs to extend Alfresco was discouraged so a new mechanism was needed.
With the release of Alfresco-content-repository 6.2.0 and Alfresco-content-repository-community 6.2.0-ea LocalTransformService was added to replace the old TransformService (now named LegacyTransformService) which may be removed in a future release.
The new LocalTransformService takes advantages of moving the Transformation to a different process (docker container) bringing all of the advantages of using docker containers and a much simpler way of developing and configuring a custom Transformer.

This repository contains examples for Hello World Transformer. These examples are used to guide developer to use the new way of building custom transformers.

* [Alfresco Hello-World Transformer AMP](alfresco-helloworld-transformer-amp/) .
* [Alfresco Hello-World Transformer T-engine](alfresco-helloworld-transformer-engine/).