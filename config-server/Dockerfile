FROM openjdk:21-jdk

ADD target/config-server-0.0.1-SNAPSHOT.jar config-server-0.0.1-SNAPSHOT.jar
COPY *.properties /config-server/

ENTRYPOINT ["java", "-jar", "config-server-0.0.1-SNAPSHOT.jar"]