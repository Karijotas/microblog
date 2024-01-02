# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

RUN ./mvnw clean install

CMD ["./mvnw", "clean", "install", "spring-boot:run"]
EXPOSE 8080
