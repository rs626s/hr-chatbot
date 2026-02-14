
# Architecture Overview – HR Chatbot System

## System Overview

The HR Chatbot system is designed as a **modular, containerized, multi-service application**.  
Each component runs independently in its own Docker container and communicates over a shared Docker network.

This architecture improves:
- Scalability
- Maintainability
- Deployment consistency
- Service isolation

---

## High-Level Architecture

```
                 +---------------------+
                 |       Browser       |
                 +----------+----------+
                            |
                            v
                 +---------------------+
                 |        Nginx        |
                 |   Reverse Proxy     |
                 +----------+----------+
                            |
                            v
                 +---------------------+
                 |   Spring Boot API   |
                 |  Authentication &   |
                 |  Business Logic     |
                 +----+----------+-----+
                      |          |
          +-----------+          +-----------+
          v                                  v
+--------------------+              +--------------------+
|     PostgreSQL     |              |      ChromaDB      |
|   Relational DB    |              |   Vector Database  |
+--------------------+              +--------------------+
                                           |
                                           v
                                  +--------------------+
                                  |       Ollama       |
                                  |   LLM Inference    |
                                  +--------------------+
```

---

## Components

### 1. Nginx (Reverse Proxy)

Responsibilities:
- Route incoming HTTP requests
- Provide a single entry point
- Future SSL termination

---

### 2. Spring Boot Backend

Responsibilities:
- REST API
- Authentication (JWT)
- Business logic
- Database access
- Communication with AI services

Technologies:
- Spring Boot
- Spring Security
- JPA / Hibernate

---

### 3. PostgreSQL Database

Responsibilities:
- Store users
- Store conversations
- Store messages

Persistence:
- Docker volume (`postgres_data`)

---

### 4. ChromaDB

Responsibilities:
- Vector storage
- Document retrieval
- Semantic search support

---

### 5. Ollama

Responsibilities:
- Language model inference
- Response generation

---

## Data Flow

1. User sends request from browser
2. Nginx routes request to Spring Boot
3. Spring Boot:
    - Authenticates user
    - Processes request
    - Stores or retrieves data from PostgreSQL
    - Retrieves context from ChromaDB (future)
    - Sends prompt to Ollama (future)
4. Response returned to user

---

## Networking

All services communicate through:

```
chatbot-network (Docker bridge network)
```

Services reference each other using service names:
- postgres
- chromadb
- ollama
- spring-boot-app

---

## Persistence

Persistent storage is handled using Docker volumes:

- postgres_data
- chromadb_data
- ollama_data

This ensures data survives container restarts.

---

## Security Overview

Security mechanisms include:

- JWT Authentication
- Password hashing
- Environment-based configuration
- Isolation through containers

Future improvements:
- HTTPS with Nginx
- Role-based access control
- Token refresh support

---

## Deployment Strategy

Deployment is handled using Docker Compose.

Key benefits:
- One command startup
- Consistent environments
- Easy scaling and upgrades

Command:
```
docker-compose up -d --build
```

---

## Future Enhancements

Planned improvements:

- Conversation memory storage
- RAG pipeline integration
- Monitoring and logging
- Cloud deployment
- Horizontal scaling

---

## Summary

The architecture follows a **layered and service-oriented design**:

- Presentation Layer → Nginx
- Application Layer → Spring Boot
- Data Layer → PostgreSQL
- AI Layer → ChromaDB + Ollama

This design ensures flexibility, scalability, and maintainability.