# HR Chatbot API (Docker Deployment)

## Overview

**HR Chatbot API** is a Spring Boot backend service that provides:

- Authentication (Spring Security + JWT)
- Conversation and message management
- Integration hooks for AI services
- Production database: **PostgreSQL 15** (via Docker)

This project is designed to run fully with **Docker Compose**:

- PostgreSQL runs in one container
- The Spring Boot application is **built from the Dockerfile** and runs in a second container  
  (no need to run `mvn spring-boot:run` manually)

---

## Tech Stack

- Java 17 (runtime inside container)
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL 15
- Flyway (database migrations)
- Docker + Docker Compose

---

## Project Structure

```text
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

- Docker Engine
- Docker Compose (v2)
- Git (optional, for cloning)

Verify:

```bash
docker --version
docker compose version
```

---

## Environment Configuration (.env)

Docker Compose reads values from `.env` automatically when you run `docker compose ...` from the project folder.

Create your `.env` file:

```bash
cp .env.example .env
nano .env
```

Example `.env` (adjust values as needed):

```env
# ---- Postgres (container) ----
POSTGRES_DB=chatbotdb
POSTGRES_USER=chatbot_user
POSTGRES_PASSWORD=StrongSecurePasswordHere
POSTGRES_PORT=5432

# ---- Spring Boot profile ----
SPRING_PROFILES_ACTIVE=prod

# IMPORTANT:
# Inside Docker Compose, use the *service name* "postgres" (NOT localhost)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/chatbotdb
SPRING_DATASOURCE_USERNAME=chatbot_user
SPRING_DATASOURCE_PASSWORD=StrongSecurePasswordHere

# ---- Flyway (optional; usually inherits datasource settings) ----
SPRING_FLYWAY_ENABLED=true

# ---- Security ----
JWT_SECRET=VeryStrongJWTSecretKey
JWT_EXPIRATION=86400000

# ---- Optional AI services ----
CHROMADB_URL=http://chromadb:8000
OLLAMA_BASE_URL=http://ollama:11434
```

---

## Run with Docker Compose

### Start (build + run)

```bash
docker compose up -d --build
```

### View logs

```bash
docker compose logs -f
```

### Stop

```bash
docker compose down
```

### Stop and delete volumes (deletes DB data)

```bash
docker compose down -v
```

---

## Service URLs

If running locally on your machine:

- App: `http://localhost:8080`
- PostgreSQL: `localhost:${POSTGRES_PORT}` (default `5432`)

If running on a server, replace `localhost` with the server IP / domain.

---

## API Endpoints

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
```

For protected endpoints, include the JWT token:

```text
Authorization: Bearer <token>
```

### System / health endpoints

```text
GET /
GET /system
GET /info
GET /actuator/health
```

---

## Database

### Production

- PostgreSQL 16 in Docker
- Flyway manages schema migrations
- Main tables:
  - `users`
  - `conversations`
  - `messages`

---

## Common Troubleshooting

### 1) App connects to H2 instead of Postgres

If logs show something like:

- `jdbc:h2:mem:...`
- `H2 console available ...`

Then the application is not using `prod` settings. Verify:

- `.env` contains `SPRING_PROFILES_ACTIVE=prod`
- `.env` is in the **same directory** where you run `docker compose up`
- `SPRING_DATASOURCE_URL` uses `postgres` (service name), not `localhost`

### 2) Postgres logs: `FATAL: database "chatbot_user" does not exist`

This usually happens when a healthcheck connects without specifying the database name.
Update the healthcheck in `docker-compose.yml` to include the DB:

```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER:-chatbot_user} -d ${POSTGRES_DB:-chatbotdb}"]
```

### 3) Actuator endpoints return 404 / not reachable

Check:

- the app container is running: `docker compose ps`
- port mapping exposes 8080
- Spring Actuator dependency is present
- Actuator exposure config (if restricted) in `application-prod.properties`

---

## Database Backup / Restore

Backup:

```bash
docker exec chatbot-postgres pg_dump -U chatbot_user chatbotdb > backup.sql
```

Restore:

```bash
cat backup.sql | docker exec -i chatbot-postgres psql -U chatbot_user -d chatbotdb
```

---

## Architecture (High-Level)

```text
Client / Browser
      |
Spring Boot API (Docker container)
      |
PostgreSQL 16 (Docker container)
      |
(Optional) ChromaDB / Ollama
```

---

## Contributors

Team 4  
Missouri State University  
CSC 615 Advanced Internet Programming
