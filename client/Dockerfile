# syntax=docker/dockerfile:1

FROM openjdk:8-alpine

WORKDIR /app

COPY src src
COPY target/client-1.0-SNAPSHOT.jar .
COPY target/libs libs/
COPY start.sh .
RUN ["chmod", "777", "start.sh"]

ENTRYPOINT ["java", "-cp", "/app/client-1.0-SNAPSHOT.jar:/app/libs/*", "io.grpc.example.helloworld.HelloWorldClient"]

