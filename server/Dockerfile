# syntax=docker/dockerfile:1

FROM openjdk:8-alpine

WORKDIR /app

COPY src src
COPY target/server-1.0-SNAPSHOT-distribution/lib .

ENTRYPOINT ["java", "-cp", "/app/server-1.0-SNAPSHOT.jar:/app/*", "io.grpc.example.helloworld.HelloWorldServer"]

