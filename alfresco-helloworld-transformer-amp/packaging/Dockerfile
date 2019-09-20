FROM alfresco/alfresco-content-repository:latest

USER root

# Set default docker_context. Will / Can be overriden with maven.
ARG resource_path=target

# Set default user information
ARG TOMCAT_DIR=/usr/local/tomcat

RUN rm -r ${TOMCAT_DIR}/amps

RUN mkdir -p ${TOMCAT_DIR}/amps

# Copy the amps from build context to the appropriate location for your application server
COPY ${resource_path}/amps ${TOMCAT_DIR}/amps

# Install amps on alfresco.war
RUN java -jar ${TOMCAT_DIR}/alfresco-mmt/alfresco-mmt*.jar install \
              ${TOMCAT_DIR}/amps ${TOMCAT_DIR}/webapps/alfresco -directory -nobackup -force

# USER alfresco