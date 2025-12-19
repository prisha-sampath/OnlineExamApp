# Use Tomcat 11 with Java 17
FROM tomcat:11.0-jdk17

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file into Tomcat
COPY target/OnlineExamApp.war /usr/local/tomcat/webapps/ROOT.war

# Expose port Render expects
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
