# Hello World T-Engine

## Purpose
This is an example Hello World transformer used to demonstrate how to create a simple transformer based on
[alfresco-transform-core](https://github.com/Alfresco/alfresco-transform-core).
The instructions to follow with this example can be found [here](https://github.com/Alfresco/acs-packaging/blob/master/docs/creating-a-t-engine.md).

## Prerequisites
* Java 11
* Maven
* Docker

## Build using Maven
The project can be built by running the Maven command. Docker must be running.
```bash
mvn clean install -Plocal
```
This will build the project as a Spring Boot fat jar in the target folder
and as a docker image in the local docker registry.

Before proceeding to start the container, confirm that the build was successful and the local docker
registry contains **alfresco/helloworld-t-engine** image.

## Run in Docker

Execute the following commands to run the transformer container in detached mode on port 8090 and to show the logs:

```bash
docker run -d -p 8090:8090 --name helloworld-t-engine alfresco/helloworld-t-engine:latest
docker logs -f helloworld-t-engine
```

## Run the Spring Boot Application

Since the T-Engine is a Spring Boot application, it might be helpful to run it as such during development by executing
one of the following:
* `mvn spring-boot:run`
* `java -jar target/helloworld-t-engine-{version}.jar` in the project directory.
* Run or debug the application `org.alfresco.transform.base.Application` from within an IDE.


## Test page

The application will be accessible on port 8090 and the test page is: `http://localhost:8090/`.
The config is available on `http://localhost:8090/transform/config`.

The [engine_config.json](src/main/resources/engine_config.json) says the source text file should be no
more than 50 bytes. If you attempt to upload a larger file the following error is logged:
```
No transforms for: text/plain -> text/html language=English
```

* Create a test file and upload it.
* You will find you need to specify the source mimetype (`text/plain` or select the `txt` extension) and target
mimetype (`text/html` or select `html`).
* Try a language value (`English`, `spanish`, `German`, `Pirate`, none).
* Press `[Transform]`

## Additional notes

##### Using custom package names
The Hello World example uses Spring dependency injection to wire beans.
For this to work, Spring component scan needs to include the 'org.alfresco.transform' package, which it does by default.
It is ok to change this package name, but the 'org.alfresco.transformer' will need to be added back for
component scanning. Spring provides several ways to do this, one of which is to add @ComponentScan annotation
to the Application with the new package and the 'org.alfresco.transformer' package configured for component scanning.

##### Issues with fabric8-maven-plugin
Ensure that the Docker installation has Docker Experimental Features enabled.
