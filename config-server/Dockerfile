FROM openjdk:21-jdk

COPY . .
COPY *.properties /config-server/

ENTRYPOINT ["java", "-jar", "target/config-server-0.0.1-SNAPSHOT.jar"]