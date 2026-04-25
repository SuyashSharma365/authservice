FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/authservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]