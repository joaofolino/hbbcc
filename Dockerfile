FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/hbbcc-0.0.1-SNAPSHOT.jar /app/hbbcc.jar

ENTRYPOINT ["java", "-jar", "hbbcc.jar"]