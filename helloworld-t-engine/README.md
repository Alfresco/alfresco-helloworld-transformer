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
registry contains the **alfresco/helloworld-t-engine** image.

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
The base T-Engine uses Spring dependency injection to auto wire beans. By default, only the
`org.alfresco.transform` package and sub-packages are scanned. Spring provides [several](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/ComponentScan.html)
ways to add additional packages if you don't wish to use this package for your own components.

##### Issues with fabric8-maven-plugin
Ensure that the Docker installation has Docker Experimental Features enabled.
