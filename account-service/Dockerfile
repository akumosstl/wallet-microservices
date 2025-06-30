FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} account-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/account-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8282