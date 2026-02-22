# HR Chatbot Application

## Overview

The HR Chatbot API is a Spring Boot backend service that provides:

-   Spring Security authentication
-   JWT based stateless authorization
-   BCrypt password hashing
-   Conversation management
-   PostgreSQL database with Flyway migrations
-   Integration with AI services (ChromaDB and Ollama)

The system supports local development and production deployment using
Docker.

------------------------------------------------------------------------

## Key Features

-   Spring Security with JWT authentication
-   Secure password hashing using BCrypt
-   PostgreSQL with Flyway schema versioning
-   Docker based infrastructure
-   Environment based configuration using .env
-   Actuator health and monitoring endpoints

------------------------------------------------------------------------

## Project Structure

hr-chatbot-api/ src/ Dockerfile docker-compose.yml .env .env.example
init-scripts/ nginx/ docs/ pom.xml

------------------------------------------------------------------------

## Prerequisites

Local Development: - Java 17 - Maven - Docker optional

Server Deployment: - Linux server - Docker Engine - Docker Compose - Git

------------------------------------------------------------------------

## Running Locally

Default profile is local.

Build the project:

mvn clean install

Run the application:

mvn spring-boot:run

Application URL:

http://localhost:8080

H2 Console:

http://localhost:8080/h2-console

Local profile uses H2 database with automatic schema creation.

------------------------------------------------------------------------

## Running with PostgreSQL (Production Mode)

Step 1: Create environment file

cp .env.example .env nano .env

Example .env:

POSTGRES_DB=chatbotdb POSTGRES_USER=chatbot_user
POSTGRES_PASSWORD=StrongSecurePasswordHere POSTGRES_PORT=5432

SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/chatbotdb
SPRING_DATASOURCE_USERNAME=chatbot_user
SPRING_DATASOURCE_PASSWORD=StrongSecurePasswordHere

JWT_SECRET=VeryStrongJWTSecretKey JWT_EXPIRATION=86400000

Step 2: Start PostgreSQL

docker compose up -d

Step 3: Run Spring Boot

export \$(grep -v '\^#' .env \| xargs) mvn spring-boot:run

Production profile uses PostgreSQL and Flyway with ddl-auto set to
validate.

------------------------------------------------------------------------

## Full Docker Deployment

Build and run all services:

docker compose up -d --build

------------------------------------------------------------------------

## API Endpoints

Authentication:

POST /api/auth/register POST /api/auth/login

Include JWT token in Authorization header:

Authorization: Bearer `<token>`{=html}

System endpoints:

GET / GET /system GET /info GET /actuator/health

------------------------------------------------------------------------

## Database

Local: H2 in memory database

Production: PostgreSQL 16 running in Docker Flyway manages schema
migrations

Tables: users conversations messages

------------------------------------------------------------------------

## Environment Variables

Database: POSTGRES_DB POSTGRES_USER POSTGRES_PASSWORD POSTGRES_PORT

Spring Boot: SPRING_PROFILES_ACTIVE SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME SPRING_DATASOURCE_PASSWORD

Security: JWT_SECRET JWT_EXPIRATION

AI Services: CHROMADB_URL OLLAMA_BASE_URL

------------------------------------------------------------------------

## Deployment on Server

Install Docker:

docker --version docker compose version

Clone project:

git clone `<repository-url>`{=html} cd hr-chatbot-api

Configure environment:

cp .env.example .env nano .env

Start services:

docker compose up -d

Run application:

export \$(grep -v '\^#' .env \| xargs) mvn spring-boot:run

------------------------------------------------------------------------

## Updating Deployment

git pull mvn clean package

If using full Docker:

docker compose down docker compose up -d --build

------------------------------------------------------------------------

## Stop Services

docker compose down

------------------------------------------------------------------------

## Database Backup

Backup:

docker exec chatbot-postgres pg_dump -U chatbot_user chatbotdb \>
backup.sql

Restore:

cat backup.sql \| docker exec -i chatbot-postgres psql -U chatbot_user
-d chatbotdb

------------------------------------------------------------------------

## Architecture Overview

Nginx \| Spring Boot Application \| PostgreSQL Database ChromaDB Ollama

------------------------------------------------------------------------

## Contributors

Team 4 Missouri State University CSC 615 Advanced Internet Programming
