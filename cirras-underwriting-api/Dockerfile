FROM tomcat:8.5.20-jre8

COPY ./cirras-underwriting-api-rest-endpoints/target/cirras-underwriting-api-rest-endpoints-3.2.0-SNAPSHOT.war .
COPY ./deploy-tools ./deploy-tools

RUN ls

ENV TOMCAT_HOME=/usr/local/tomcat \
  CATALINA_HOME=/usr/local/tomcat \
  CATALINA_OPTS='-Xmx400m -Xms200m -server'

RUN rm -rf /usr/local/tomcat/webapps/ROOT  &&\
  mkdir /usr/local/tomcat/webapps/cirras-underwriting-api#v1 &&\
  unzip -d /usr/local/tomcat/webapps/cirras-underwriting-api#v1/ '*.war' &&\
  cp ./deploy-tools/context.xml /usr/local/tomcat/webapps/cirras-underwriting-api#v1/META-INF/context.xml &&\
  cp ./deploy-tools/setenv.sh /usr/local/tomcat/bin/setenv.sh &&\
  adduser --system tomcat &&\
  chown -R tomcat:0 `readlink -f ${CATALINA_HOME}` &&\
  chmod -R 770 `readlink -f ${CATALINA_HOME}` &&\
  chown -h tomcat:0 ${CATALINA_HOME} &&\
  mv /etc/localtime /etc/localtime-old &&\
  ln -s /usr/share/zoneinfo/America/Vancouver /etc/localtime 

USER tomcat

EXPOSE 8005 8080
CMD ["catalina.sh", "run"]