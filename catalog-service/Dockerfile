FROM openjdk:21-jdk
FROM maven:3.8.8-amazoncorretto-21-al2023

COPY . .

ENTRYPOINT ["java", "-jar", "target/catalog-service-0.0.1-SNAPSHOT.jar"]