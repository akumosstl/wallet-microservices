FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} transaction-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/transaction-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8284