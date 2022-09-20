# Alfresco Hello World Transformer Examples

This repository contains three example Hello World Transformers:

* [Hello-World T-Engine](helloworld-t-engine/) a T-Engine that extends the
  `alfresco-base-t-engine`;
* [Alfresco Hello-World Transformer T-engine](alfresco-helloworld-transformer-engine/) a T-Engine that
  extends the deprecated [alfresco-transformer-base](https://github.com/Alfresco/alfresco-transform-core/blob/master/deprecated/alfresco-transformer-base/);
* [Alfresco Hello-World Transformer AMP](alfresco-helloworld-transformer-amp/) a legacy transformer as an AMP.

In Alfresco Content Service 6.0, the Enterprise edition allowed transforms to run in separate docker containers,
known as T-Engines. Transforms could still run within the content repository itself, now named Legacy transforms.

In Alfresco Content Service 6.2 both Community and Enterprise editions were able to
use transforms in docker containers in addition to legacy transforms.

In ACS 7.0.0, Legacy transforms were removed.

The `alfresco-base-t-engine` simplifies the creation of T-Engines. It was introduced as a
module of `alfresco-transform-core` 3.0.0.
