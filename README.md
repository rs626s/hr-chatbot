
# HR Chatbot API

## Overview
The HR Chatbot API is a Spring Boot–based backend service that provides authentication, conversation management, and integration with AI services.  
The system is designed to run in Docker using a multi-container architecture.

Services included:
- Spring Boot API
- PostgreSQL database
- ChromaDB (vector database)
- Ollama (LLM service)
- Nginx (reverse proxy)

---

## Project Structure

```
hr-chatbot-api/
├── src/
├── Dockerfile
├── docker-compose.yml
├── .env
├── init-scripts/
├── nginx/
├── docs/
└── pom.xml
```

---

## Prerequisites

Local Development:
- Java 17
- Maven
- Docker Desktop (optional for container testing)

Server Deployment:
- Docker
- Docker Compose v2+
- Git

---

## Running Locally (Without Docker)

1. Build the project:
```
mvn clean install
```

2. Run the application:
```
mvn spring-boot:run
```

Application will start at:
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

Update passwords and secrets.

---

### Step 3: Build and start services
```
docker compose up -d --build
```

---

### Step 4: Verify services
```
docker compose ps
```

Check logs:
```
docker compose logs -f
```

---

## API Endpoints

Authentication:

Register:
```
POST /api/auth/register
```

Login:
```
POST /api/auth/login
```

Health Check:
```
GET /actuator/health
```

---

## Database

Local:
- H2 in-memory database

Docker / Production:
- PostgreSQL container

Schema includes:
- Users
- Conversations
- Messages

---

## Environment Variables

Configured in `.env`:

- POSTGRES_DB
- POSTGRES_USER
- POSTGRES_PASSWORD
- SPRING_DATASOURCE_URL
- JWT_SECRET
- CHROMADB_URL
- OLLAMA_BASE_URL

---

## Deployment on Server

1. Install Docker
2. Clone repository
3. Configure `.env`
4. Run:

```
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

```
docker exec chatbot-postgres pg_dump -U chatbot_user chatbotdb > backup.sql
```

Restore:

```
cat backup.sql | docker exec -i chatbot-postgres psql -U chatbot_user -d chatbotdb
```

---

## Contributors

Team 4 – HR Chatbot API  
Missouri State University  
CSC 615 – AI Project

---
