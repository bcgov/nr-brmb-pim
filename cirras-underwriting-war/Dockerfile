FROM tomcat:9.0.86-jre8

COPY ./cirras-underwriting-war/target/cirras-underwriting-war-3.2.0-SNAPSHOT.war .
COPY ./cirras-underwriting-war/src/main/resources/log4j2.xml .
COPY ./deploy-tools/server.xml .

ENV TOMCAT_HOME=/usr/local/tomcat \
  CATALINA_HOME=/usr/local/tomcat \
  CATALINA_OPTS='-Xmx400m -Xms200m -server'

RUN rm -rf /usr/local/tomcat/webapps/ROOT  &&\
  mkdir /usr/local/tomcat/webapps/pub#cirras-underwriting &&\
  apt-get update && apt-get install -y unzip &&\
  unzip -d /usr/local/tomcat/webapps/pub#cirras-underwriting/ '*.war' &&\
  cp ./server.xml /usr/local/tomcat/conf/server.xml &&\
  cp ./log4j2.xml /usr/local/tomcat/webapps/pub#cirras-underwriting/WEB-INF/classes/log4j2.xml &&\
  adduser --system tomcat &&\
  chown -R tomcat:0 `readlink -f ${CATALINA_HOME}` &&\
  chmod -R 777 `readlink -f ${CATALINA_HOME}` &&\
  chown -h tomcat:0 ${CATALINA_HOME} &&\
  rm /etc/localtime &&\
  ln -s /usr/share/zoneinfo/America/Vancouver /etc/localtime &&\
  export SECRET_KEY=$(openssl rand -base64 32) &&\
  keytool -genkey -noprompt -validity 365 -alias tomcat -keyalg RSA -keystore conf/selfsigned.jks -keysize 2048 -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown" -keypass $SECRET_KEY -storepass $SECRET_KEY &&\
  sed -i "s@SECRET_KEY@$SECRET_KEY@" conf/server.xml

USER tomcat

EXPOSE 8080
CMD ["catalina.sh", "run"]