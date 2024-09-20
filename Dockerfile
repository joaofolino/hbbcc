FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/hbbcc-0.0.1-SNAPSHOT.jar /app/hbbcc.jar

ENTRYPOINT ["java", "-jar", "hbbcc.jar"]