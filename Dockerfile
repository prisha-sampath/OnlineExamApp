# -------- BUILD STAGE --------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- RUNTIME STAGE --------
FROM tomcat:10.1-jdk17


# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Disable shutdown port (fixes Render warnings)
RUN sed -i 's/port="8005"/port="-1"/' $CATALINA_HOME/conf/server.xml

# Copy WAR from build stage
COPY --from=build /app/target/OnlineExamApp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
