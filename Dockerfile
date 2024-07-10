FROM maven:3.6.3-jdk-11 AS builder
WORKDIR /tmp
COPY ./src ./src
COPY ./pom.xml .
RUN mvn package
FROM tomcat:9.0.71-jdk11-temurin-focal
ENV WAR_FILE /tmp/target/app.war
COPY --from=builder ${WAR_FILE} /usr/local/tomcat/webapps/
