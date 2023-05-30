# Image provides a container in which to run the example Hello World docker transformer.

FROM alfresco/alfresco-base-java:jre17-rockylinux8-202305301003

ENV JAVA_OPTS=""

# Set default user information
ARG GROUPNAME=Alfresco
ARG GROUPID=1000
ARG HWUSERNAME=transform-helloworld
ARG USERID=33004

COPY target/alfresco-helloworld-transformer-${env.project_version}.jar /usr/bin

RUN ln /usr/bin/alfresco-helloworld-transformer-${env.project_version}.jar /usr/bin/alfresco-helloworld-transformer.jar && \
    yum clean all

RUN groupadd -g ${GROUPID} ${GROUPNAME} && \
    useradd -u ${USERID} -G ${GROUPNAME} ${HWUSERNAME} && \
    chgrp -R ${GROUPNAME} /usr/bin/alfresco-helloworld-transformer.jar

EXPOSE 8090

USER ${HWUSERNAME}

ENTRYPOINT java $JAVA_OPTS -jar /usr/bin/alfresco-helloworld-transformer.jar
