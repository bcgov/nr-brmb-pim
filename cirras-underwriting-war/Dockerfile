FROM tomcat:9.0.86-jre8

COPY ./cirras-underwriting-war/target/cirras-underwriting-war-3.2.0-SNAPSHOT.war .
COPY ./cirras-underwriting-war/src/main/resources/log4j2.xml .

ENV TOMCAT_HOME=/usr/local/tomcat \
  CATALINA_HOME=/usr/local/tomcat \
  CATALINA_OPTS='-Xmx400m -Xms200m -server'

RUN rm -rf /usr/local/tomcat/webapps/ROOT  &&\
  mkdir /usr/local/tomcat/webapps/pub#cirras-underwriting &&\
  apt-get update && apt-get install -y unzip &&\
  unzip -d /usr/local/tomcat/webapps/pub#cirras-underwriting/ '*.war' &&\
  cp ./log4j2.xml /usr/local/tomcat/webapps/pub#cirras-underwriting/WEB-INF/classes/log4j2.xml &&\
  adduser --system tomcat &&\
  chown -R tomcat:0 `readlink -f ${CATALINA_HOME}` &&\
  chmod -R 777 `readlink -f ${CATALINA_HOME}` &&\
  chown -h tomcat:0 ${CATALINA_HOME} &&\
  mv /etc/localtime /etc/localtime-old &&\
  ln -s /usr/share/zoneinfo/America/Vancouver /etc/localtime 

USER tomcat

EXPOSE 8080
CMD ["catalina.sh", "run"]