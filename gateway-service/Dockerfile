FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} gateway-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/gateway-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8180