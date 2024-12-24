# syntax=docker/dockerfile:1

FROM maven:3.8.4-openjdk-8 AS builder
COPY . /grpc-study/
RUN mvn clean package -f /grpc-study && unzip /grpc-study/server/target/*.zip


FROM openjdk:8-alpine
WORKDIR /app

COPY --from=builder /grpc-study/server/target/server-1.0-SNAPSHOT-distribution/lib .

ENTRYPOINT ["java", "-cp", "/app/server-1.0-SNAPSHOT.jar:/app/*", "io.grpc.example.helloworld.HelloWorldServer"]

