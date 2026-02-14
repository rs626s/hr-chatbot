
# HR Chatbot API Documentation

## Base URL

```
http://localhost:8080/api
```

When deployed behind Nginx, the base URL may be:

```
http://localhost/api
```

---

## Authentication

Authentication uses **JWT (JSON Web Token)**.

After login or registration, a token is returned.  
Include the token in subsequent requests:

```
Authorization: Bearer <token>
```

---

# Endpoints

## 1. Register User

**Endpoint**

```
POST /api/auth/register
```

**Request Body**

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

**Response (201 Created)**

```json
{
  "token": "jwt_token_here",
  "type": "Bearer",
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

---

## 2. Login

**Endpoint**

```
POST /api/auth/login
```

**Request Body**

```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Success Response (201 Created)**

```json
{
  "token": "jwt_token_here",
  "type": "Bearer",
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

**Error Response (401 Unauthorized)**

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

---

## 3. Validation Errors

If request validation fails:

**Response (400 Bad Request)**

```json
{
  "fieldName": "validation message"
}
```

Example:

```json
{
  "username": "must not be blank"
}
```

---

# Error Handling

The API returns structured error responses:

| Status | Meaning |
|--------|--------|
| 400 | Validation or bad request |
| 401 | Unauthorized |
| 500 | Internal server error |

---

# Health Check

**Endpoint**

```
GET /actuator/health
```

**Response**

```json
{
  "status": "UP"
}
```

---

# Future Endpoints (Planned)

These endpoints will be implemented in later sprints:

- Conversation management
- Chat messaging
- Document embedding and retrieval
- AI response generation

---

# Testing with cURL

Example login request:

```
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

---

# Technology Stack

- Spring Boot
- Spring Security
- JWT Authentication
- PostgreSQL
- Docker & Docker Compose
- ChromaDB
- Ollama
- Nginx