FROM tomcat:8.5-jdk8

COPY ./cirras-underwriting-api-rest-endpoints/target/cirras-underwriting-api-rest-endpoints-3.2.0-SNAPSHOT.war .
COPY ../deploy-tools ./deploy-tools

RUN ls

ENV TOMCAT_HOME=/usr/local/tomcat \
  CATALINA_HOME=/usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/ROOT  &&\
  mkdir /usr/local/tomcat/webapps/cirras-underwriting-api#v1 &&\
  apt-get update &&\
  apt-get install -y unzip &&\
  unzip -d /usr/local/tomcat/webapps/cirras-underwriting-api#v1/ '*.war' &&\
  cp ./deploy-tools/context.xml /usr/local/tomcat/webapps/cirras-underwriting-api#v1/META-INF/context.xml &&\
  cp ./deploy-tools/setenv.sh /usr/local/tomcat/bin/setenv.sh &&\
  cp ./deploy-tools/server.xml /usr/local/tomcat/conf/server.xml &&\
  adduser --system tomcat &&\
  chown -R tomcat:0 `readlink -f ${CATALINA_HOME}` &&\
  chmod -R 770 `readlink -f ${CATALINA_HOME}` &&\
  chown -h tomcat:0 ${CATALINA_HOME}

USER tomcat

EXPOSE 8005 8080
CMD ["catalina.sh", "run"]