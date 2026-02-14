
# Deployment Guide

## Overview

This guide explains how to deploy the HR Chatbot system using Docker Compose.  
The system consists of multiple services running together:

- PostgreSQL (Database)
- Spring Boot Application (Backend API)
- ChromaDB (Vector Database)
- Ollama (LLM Inference Engine)
- Nginx (Reverse Proxy)

---

## Prerequisites

Before deployment, ensure the following are installed:

- Docker (version 24+ recommended)
- Docker Compose
- Git (optional but recommended)

Verify installation:

```
docker --version
docker compose version
```

---

## Project Structure

Example structure:

```
project-root/
│
├── docker-compose.yml
├── .env
├── Dockerfile
├── nginx/
│   └── nginx.conf
├── init-scripts/
│   └── init.sql
└── src/
```

---

## Step 1: Configure Environment Variables

Create a `.env` file in the project root.

Example:

```
POSTGRES_DB=chatbotdb
POSTGRES_USER=chatbot_user
POSTGRES_PASSWORD=StrongPassword123
POSTGRES_PORT=5432

SPRING_BOOT_PORT=8080
SPRING_PROFILES_ACTIVE=prod

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/chatbotdb
SPRING_DATASOURCE_USERNAME=chatbot_user
SPRING_DATASOURCE_PASSWORD=StrongPassword123

JWT_SECRET=change_this_secret
JWT_EXPIRATION=86400000

CHROMADB_PORT=8000
CHROMADB_URL=http://chromadb:8000

OLLAMA_PORT=11434
OLLAMA_BASE_URL=http://ollama:11434

NGINX_PORT=80
```

Make sure secrets are never committed to public repositories.

---

## Step 2: Database Initialization

PostgreSQL runs initialization scripts automatically from:

```
/docker-entrypoint-initdb.d/
```

Create:

```
init-scripts/init.sql
```

Example:

```
GRANT ALL PRIVILEGES ON DATABASE chatbotdb TO chatbot_user;
```

Spring Boot JPA will create tables automatically.

---

## Step 3: Build and Start Services

From the project root, run:

```
docker compose up --build
```

This will:

- Build the Spring Boot image
- Pull required images
- Start containers
- Create networks and volumes

---

## Step 4: Verify Containers

Check running containers:

```
docker ps
```

Expected services:

- chatbot-postgres
- chatbot-app
- chatbot-chromadb
- chatbot-ollama
- chatbot-nginx

---

## Step 5: Access the Application

Default URLs:

Backend:
```
http://localhost:8080
```

Through Nginx:
```
http://localhost
```

Health Check:
```
http://localhost:8080/actuator/health
```

---

## Step 6: Viewing Logs

To view logs:

```
docker compose logs -f
```

For a specific service:

```
docker compose logs -f spring-boot-app
```

---

## Step 7: Stopping Services

Stop containers:

```
docker compose down
```

Stop and remove volumes (WARNING: deletes data):

```
docker compose down -v
```

---

## Step 8: Updating the Application

When code changes:

```
docker compose up --build
```

This rebuilds only the Spring Boot container.

---

## Troubleshooting

### Database connection issues
- Ensure PostgreSQL container is healthy
- Verify `.env` credentials

### Port already in use
Check running services:

```
netstat -ano | findstr :8080
```

Or change port in `.env`.

### Container fails to start
Inspect logs:

```
docker compose logs
```

---

## Production Recommendations

For production deployments:

- Use strong secrets
- Use HTTPS (Nginx + SSL)
- Use managed database if possible
- Add monitoring (Prometheus/Grafana)
- Enable backups

---

## Deployment Workflow Summary

1. Configure `.env`
2. Prepare initialization scripts
3. Run `docker compose up --build`
4. Verify containers
5. Access application
6. Monitor logs