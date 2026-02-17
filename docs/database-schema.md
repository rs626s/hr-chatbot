# Database Schema – HR Chatbot API

## Overview

The HR Chatbot system uses a relational database managed by **Spring Data JPA / Hibernate**.

Database used:
- Local development: H2  
- Production: PostgreSQL  

Tables are generated automatically from JPA entities.

---

# Entity Relationship Diagram

User (1) ──────── (N) Conversation (1) ──────── (N) Message

---

# Tables

## 1. users

Stores registered users.

| Column | Type | Constraints |
|--------|--------|--------|
| id | BIGINT | Primary Key, Auto Increment |
| username | VARCHAR(50) | Unique, Not Null |
| email | VARCHAR(100) | Unique, Not Null |
| password_hash | VARCHAR(255) | Not Null |
| first_name | VARCHAR(50) | Nullable |
| last_name | VARCHAR(50) | Nullable |
| role | VARCHAR(20) | Not Null, Default = USER |
| enabled | BOOLEAN | Not Null, Default = TRUE |
| created_at | TIMESTAMP | Not Null, Auto Generated |
| updated_at | TIMESTAMP | Auto Updated |
| last_login | TIMESTAMP | Nullable |

### Relationships
- One user can have multiple conversations.
- Cascade: ALL (deleting a user deletes conversations).

---

## 2. conversations

Stores conversation sessions.

| Column | Type | Constraints |
|--------|--------|--------|
| id | BIGINT | Primary Key, Auto Increment |
| title | VARCHAR(200) | Nullable |
| created_at | TIMESTAMP | Not Null, Auto Generated |
| updated_at | TIMESTAMP | Auto Updated |
| user_id | BIGINT | Foreign Key → users.id, Not Null |

### Relationships
- Many conversations belong to one user.
- One conversation has many messages.
- Cascade to messages: ALL.

---

## 3. messages

Stores chat messages.

| Column | Type | Constraints |
|--------|--------|--------|
| id | BIGINT | Primary Key, Auto Increment |
| content | TEXT | Not Null |
| role | VARCHAR(20) | Not Null ("USER" or "ASSISTANT") |
| created_at | TIMESTAMP | Not Null, Auto Generated |
| conversation_id | BIGINT | Foreign Key → conversations.id, Not Null |

### Relationships
- Many messages belong to one conversation.

---

# Logical Schema Diagram

+----------------------+
|        users         |
+----------------------+
| id (PK)              |
| username (unique)    |
| email (unique)       |
| password_hash        |
| first_name           |
| last_name            |
| role                 |
| enabled              |
| created_at           |
| updated_at           |
| last_login           |
+----------------------+
           |
           | 1..N
           |
+----------------------+
|    conversations     |
+----------------------+
| id (PK)              |
| title                |
| created_at           |
| updated_at           |
| user_id (FK)         |
+----------------------+
           |
           | 1..N
           |
+----------------------+
|       messages       |
+----------------------+
| id (PK)              |
| content              |
| role                 |
| created_at           |
| conversation_id (FK) |
+----------------------+


---

# JPA Mapping Notes

User → Conversation  
One user can have many conversations. Cascade operations ensure conversations are removed if the user is deleted.

Conversation → Message  
One conversation can have many messages. Cascade operations ensure messages are removed if the conversation is deleted.

Fetch Type  
LAZY loading is used for relationships to improve performance.

---

# Hibernate Features Used

- @CreationTimestamp automatically sets creation time.
- @UpdateTimestamp automatically updates modification time.
- Cascade operations maintain referential integrity.

---

# Indexing Recommendations (Next Sprint)

Suggested indexes:
- users(username)
- users(email)
- conversations(user_id)
- messages(conversation_id)

---

# Notes

- Passwords are stored as hashed values (BCrypt).
- Role field controls authorization (USER / ADMIN).
- H2 database is used for development.
- PostgreSQL will be used in production.
- Schema is automatically managed by Hibernate from entity definitions.
