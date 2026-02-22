# PostgreSQL Docker Setup Guide

HR Chatbot -- Production Database Configuration

## Overview

This document explains how to set up PostgreSQL using Docker for the HR
Chatbot project and connect it to the Spring Boot application using the
production profile.

Architecture: - Docker → PostgreSQL - Maven → Spring Boot (prod
profile) - Flyway manages schema - Hibernate validates schema

------------------------------------------------------------------------

## Prerequisites

-   Docker installed
-   Docker Compose installed
-   Java 17+
-   Maven
-   Project cloned to server

------------------------------------------------------------------------

## Step 1: Create .env File

Create a `.env` file in the same directory as `docker-compose.yml`.

POSTGRES_DB=chatbotdb POSTGRES_USER=chatbot_user
POSTGRES_PASSWORD=StrongSecurePasswordHere POSTGRES_PORT=5432

SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/chatbotdb
SPRING_DATASOURCE_USERNAME=chatbot_user
SPRING_DATASOURCE_PASSWORD=StrongSecurePasswordHere

JWT_SECRET=VeryStrongJWTSecretKeyAtLeast256BitsLong
JWT_EXPIRATION=86400000

IMPORTANT: Never commit `.env` to Git.

------------------------------------------------------------------------

## Step 2: Start PostgreSQL

docker-compose up -d

Check status: docker-compose ps

------------------------------------------------------------------------

## Step 3: Verify Database

docker exec -it chatbot-postgres psql -U chatbot_user -d chatbotdb

------------------------------------------------------------------------

## Step 4: Run Application

Load environment variables: export \$(grep -v '\^#' .env \| xargs)

Start application: mvn spring-boot:run

------------------------------------------------------------------------

## Production Rules

-   Use ddl-auto=validate
-   Use Flyway migrations
-   Never hardcode passwords
-   Never commit .env
