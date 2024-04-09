FROM tomcat:8.5.47-jdk11-openjdk

COPY ./cirras-underwriting-war/target/cirras-underwriting-war-3.2.0-SNAPSHOT.war .
COPY ./cirras-underwriting-war/src/main/resources/log4j2.xml .

ENV TOMCAT_HOME=/usr/local/tomcat \
  CATALINA_HOME=/usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/ROOT  &&\
  mkdir /usr/local/tomcat/webapps/pub#cirras-underwriting &&\
  unzip -d /usr/local/tomcat/webapps/pub#cirras-underwriting/ '*.war' &&\
  cp ./log4j2.xml /usr/local/tomcat/webapps/pub#cirras-underwriting/WEB-INF/classes/log4j2.xml &&\
  adduser --system tomcat &&\
  chown -R tomcat:0 `readlink -f ${CATALINA_HOME}` &&\
  chmod -R 770 `readlink -f ${CATALINA_HOME}` &&\
  chown -h tomcat:0 ${CATALINA_HOME}

USER tomcat

EXPOSE 8080
CMD ["catalina.sh", "run"]