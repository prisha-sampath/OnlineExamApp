FROM tomcat:11-jdk11

# Disable shutdown port (optional but good)
RUN sed -i 's/port="8005"/port="-1"/' $CATALINA_HOME/conf/server.xml

# Copy WAR
COPY target/*.war $CATALINA_HOME/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
