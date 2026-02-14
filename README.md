# HR Chatbot API

## Overview

The HR Chatbot API is a Spring Boot–based backend service that provides authentication, conversation management, and integration with AI services.

The system is designed to run in Docker using a multi-container architecture and supports deployment both locally and on a Linux server.

Services included:

* Spring Boot API
* PostgreSQL database
* ChromaDB (vector database)
* Ollama (LLM service)
* Nginx (reverse proxy)

---

## Project Structure

```
hr-chatbot-api/
├── src/
├── Dockerfile
├── docker-compose.yml
├── .env
├── .env.example
├── init-scripts/
├── nginx/
├── docs/
└── pom.xml
```

---

## Prerequisites

### Local Development

* Java 17
* Maven
* Docker Desktop (optional)

### Server Deployment

* Ubuntu / Linux server
* Docker Engine
* Docker Compose v2+
* Git

---

## Running Locally (Without Docker)

### Build the project

```
mvn clean install
```

### Run the application

```
mvn spring-boot:run
```

Application:

```
http://localhost:8080
```

H2 Console (local only):

```
http://localhost:8080/h2-console
```

---

## Running with Docker Compose

### Step 1: Clone repository

```
git clone <repository-url>
cd hr-chatbot-api
```

### Step 2: Create environment file

```
cp .env.example .env
nano .env
```

Update:

* Database password
* JWT secret
* Ports if required

---

### Step 3: Build and start services

```
docker compose up -d --build
```

---

### Step 4: Verify containers

```
docker ps
```

Check logs:

```
docker compose logs -f
```

---

## Service Verification

### API Health

```
curl http://localhost:8080/actuator/health
```

---

### Root Endpoint

```
curl http://localhost:8080/
```

Response:

```
{
  "service": "HR Chatbot API",
  "status": "running",
  "message": "API is up and healthy"
}
```

---

### System Status

```
curl http://localhost:8080/system
```

Shows:

* Database status
* ChromaDB status
* Ollama status

---

### Application Info

```
curl http://localhost:8080/info
```

Shows:

* Application name
* Environment
* Timestamp

---

## API Endpoints

### Authentication

Register:

```
POST /api/auth/register
```

Login:

```
POST /api/auth/login
```

---

### System Endpoints

Root:

```
GET /
```

System health:

```
GET /system
```

Application info:

```
GET /info
```

Spring Boot health:

```
GET /actuator/health
```

---

## Database

### Local

* H2 in-memory database

### Docker / Production

* PostgreSQL container

Schema includes:

* Users
* Conversations
* Messages

---

## Environment Variables

Configured in `.env`:

Database:

* POSTGRES_DB
* POSTGRES_USER
* POSTGRES_PASSWORD

Spring Boot:

* SPRING_DATASOURCE_URL
* SPRING_DATASOURCE_USERNAME
* SPRING_DATASOURCE_PASSWORD
* SPRING_PROFILES_ACTIVE

Security:

* JWT_SECRET
* JWT_EXPIRATION

AI Services:

* CHROMADB_URL
* OLLAMA_BASE_URL

Ports:

* SPRING_BOOT_PORT
* POSTGRES_PORT
* CHROMADB_PORT
* OLLAMA_PORT
* NGINX_PORT

---

## Deployment on Server

### Step 1: Install Docker

Verify:

```
docker --version
docker compose version
```

---

### Step 2: Clone repository

```
git clone <repository-url>
cd hr-chatbot-api
```

---

### Step 3: Configure environment

```
cp .env.example .env
nano .env
```

---

### Step 4: Build and deploy

```
docker compose up -d --build
```

---

### Step 5: Verify deployment

```
docker ps
curl http://localhost:8080/actuator/health
curl http://localhost:8080/system
```

---

## Updating Deployment

If code changes:

```
git pull
docker compose down
docker compose up -d --build
```

---

## Stopping Services

```
docker compose down
```

Volumes remain intact.

---

## Backup Database

Backup:

```
docker exec chatbot-postgres pg_dump -U chatbot_user chatbotdb > backup.sql
```

Restore:

```
cat backup.sql | docker exec -i chatbot-postgres psql -U chatbot_user -d chatbotdb
```

---

## Architecture Overview

```
                +-------------+
                |   Nginx     |
                +------+------+
                       |
                +------+------+
                | Spring Boot |
                +------+------+
                       |
        +--------------+--------------+
        |                             |
+-------+--------+           +--------+-------+
| PostgreSQL DB  |           |  ChromaDB      |
+----------------+           +----------------+
                       |
                 +-----+-----+
                 |   Ollama  |
                 +-----------+
```

---

## Contributors

Team 4 – HR Chatbot API
Missouri State University
CSC 615 – AI Project

---
