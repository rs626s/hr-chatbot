# Server Setup Guide

HR Chatbot -- Server Preparation

## 1. Install Required Tools

sudo apt update sudo apt install docker.io docker-compose maven
openjdk-17-jdk -y

Verify: docker --version mvn -version java -version

------------------------------------------------------------------------

## 2. Clone Project

git clone `<your-repository-url>`{=html} cd hr-chatbot

------------------------------------------------------------------------

## 3. Configure Environment

cp .env.example .env nano .env

Set secure values for: - POSTGRES_PASSWORD - JWT_SECRET

------------------------------------------------------------------------

## 4. Start PostgreSQL

docker-compose up -d

------------------------------------------------------------------------

## 5. Run Application

export \$(grep -v '\^#' .env \| xargs) mvn spring-boot:run

------------------------------------------------------------------------

## 6. Verify Application

curl http://localhost:8080/actuator/health

Server setup complete.
