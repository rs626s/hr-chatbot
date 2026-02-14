
# Spring Boot Dockerfile Design Plan

## Overview

The HR Chatbot backend is packaged as a Spring Boot application and deployed inside a Docker container.

Goals:
- Small image size
- Fast startup
- Secure runtime
- Easy configuration using environment variables

---

## Multi-Stage Build Strategy

A multi-stage Docker build is used to reduce image size.

### Stage 1: Build Stage

Purpose:
- Compile application
- Package JAR file

Base Image:
```
maven:3.9-eclipse-temurin-17
```

Steps:
1. Copy pom.xml
2. Download dependencies
3. Copy source code
4. Build application using Maven

Output:
```
target/*.jar
```

---

### Stage 2: Runtime Stage

Purpose:
- Run application in lightweight environment

Base Image:
```
eclipse-temurin:17-jre-alpine
```

Benefits:
- Smaller image
- Faster deployment
- No build tools included

---

## Recommended Dockerfile Structure

Example:

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
```

---

## Environment Variables

Configured via Docker Compose:

Required:

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- JWT_SECRET
- SPRING_PROFILES_ACTIVE

Optional:

- SPRING_JPA_SHOW_SQL
- SERVER_PORT

Environment variables override application.properties.

---

## Health Check Strategy

Spring Boot provides Actuator health endpoint.

Endpoint:
```
/actuator/health
```

Docker healthcheck example:

```
wget --spider http://localhost:8080/actuator/health
```

Benefits:
- Container orchestration awareness
- Safe service startup order

---

## Security Best Practices

Applied practices:

- Run container as non-root user
- Avoid embedding secrets in image
- Use environment variables
- Keep base image minimal

---

## Performance Considerations

Optimizations:

- Dependency caching
- Multi-stage builds
- Lightweight runtime image

---

## Networking

Container communicates internally with:

- PostgreSQL → postgres:5432
- ChromaDB → chromadb:8000
- Ollama → ollama:11434

No hardcoded IP addresses used.

---

## Logging

Logs are written to:
```
stdout
```

This allows Docker to manage logs using:
```
docker logs chatbot-app
```

---

## Summary

Spring Boot container provides:

- Lightweight runtime image
- Secure execution
- Externalized configuration
- Health monitoring
- Seamless Docker Compose integration
