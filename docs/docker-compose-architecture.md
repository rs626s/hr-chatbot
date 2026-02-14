
# Docker Compose Architecture

## Overview

The HR Chatbot system runs as a multi-container application orchestrated using Docker Compose. Each service has a clear responsibility and communicates over a shared internal network.

Services included:

1. PostgreSQL – Relational database
2. Spring Boot Application – Backend API
3. ChromaDB – Vector database
4. Ollama – LLM inference engine
5. Nginx – Reverse proxy

---

## High-Level Architecture Diagram

```
                        ┌───────────────────────┐
                        │      Client / UI      │
                        │  (Browser / Postman)  │
                        └──────────┬────────────┘
                                   │ HTTP
                                   ▼
                        ┌───────────────────────┐
                        │        Nginx          │
                        │   Reverse Proxy       │
                        └──────────┬────────────┘
                                   │
                                   ▼
                        ┌───────────────────────┐
                        │    Spring Boot API    │
                        │ Authentication + API  │
                        └───────┬───────┬───────┘
                                │       │
              ┌─────────────────┘       └─────────────────┐
              ▼                                           ▼
   ┌───────────────────────┐                 ┌───────────────────────┐
   │      PostgreSQL       │                 │       ChromaDB        │
   │  Users / Messages DB  │                 │  Embeddings / Vectors │
   └───────────────────────┘                 └───────────────────────┘
                                │
                                ▼
                       ┌───────────────────────┐
                       │        Ollama         │
                       │   LLM Inference       │
                       └───────────────────────┘
```

---

## Service Responsibilities

### PostgreSQL
Responsible for structured data storage.

Stores:
- Users
- Conversations
- Messages

Features:
- Persistent volume storage
- Database initialization via `init.sql`
- Health checks before dependent services start

---

### Spring Boot Application
Core backend service.

Responsibilities:
- REST API endpoints
- Authentication (JWT)
- Business logic
- Communication with:
    - PostgreSQL
    - ChromaDB
    - Ollama

Features:
- Health endpoint for Docker health checks
- Environment-based configuration

---

### ChromaDB
Vector database used for retrieval and embeddings.

Responsibilities:
- Store document embeddings
- Perform semantic similarity search

---

### Ollama
Runs local language models.

Responsibilities:
- Model inference
- Answer generation
- Interaction with backend APIs

---

### Nginx
Acts as the entry point to the system.

Responsibilities:
- Reverse proxy
- Route traffic to backend
- Central access point

---

## Networking

All services communicate using a shared Docker bridge network:

```
chatbot-network
```

Benefits:
- Service discovery by container name
- Isolation from host network
- Secure internal communication

Example connection string used by Spring Boot:

```
jdbc:postgresql://postgres:5432/chatbotdb
```

---

## Persistence

Docker volumes ensure data is not lost when containers restart.

Volumes used:

- postgres_data – Database storage
- chromadb_data – Vector storage
- ollama_data – Model storage

---

## Container Startup Order

Docker Compose ensures proper startup sequence:

1. PostgreSQL starts first
2. ChromaDB and Ollama start
3. Spring Boot waits for dependencies
4. Nginx starts after backend is healthy

This prevents connection failures at startup.

---

## Deployment Strategy

Docker Compose is used to:

- Build Spring Boot container
- Pull service images automatically
- Start containers in correct order
- Manage networking
- Persist data using volumes
- Run health checks

Deployment command:

```
docker-compose up --build
```

Stop containers:

```
docker-compose down
```

---