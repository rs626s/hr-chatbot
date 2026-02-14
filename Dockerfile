FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

RUN apt-get update && apt-get install -y git

RUN git clone https://github.com/rs626s/hr-chatbot-api.git

WORKDIR /app/hr-chatbot-api
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/hr-chatbot-api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
