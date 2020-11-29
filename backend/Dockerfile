FROM maven:3.5-jdk-8 as BUILD

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml install

FROM openjdk:8-jre-alpine

COPY --from=BUILD /usr/src/app/target/*.jar  /usr/app/app.jar
WORKDIR /usr/app

RUN apk add --no-cache bash
