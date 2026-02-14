
# Database Schema – HR Chatbot API

## Overview

The HR Chatbot system uses a relational database managed by **Spring Data JPA / Hibernate**.

Database used:
- Local development: H2
- Production: PostgreSQL

Tables are generated automatically from JPA entities.

---

# Entity Relationship Diagram

```
User (1) ──────── (N) Conversation (1) ──────── (N) Message
```

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
| role | VARCHAR | Not Null, Default = USER |

### Relationships
- One user can have multiple conversations
- Cascade: ALL (deleting a user deletes conversations)

---

## 2. conversations

Stores conversation sessions.

| Column | Type | Constraints |
|--------|--------|--------|
| id | BIGINT | Primary Key, Auto Increment |
| title | VARCHAR(200) | Nullable |
| created_at | TIMESTAMP | Not Null, Default = current time |
| user_id | BIGINT | Foreign Key → users.id, Not Null |

### Relationships
- Many conversations belong to one user
- One conversation has many messages
- Cascade to messages: ALL

---

## 3. messages

Stores chat messages.

| Column | Type | Constraints |
|--------|--------|--------|
| id | BIGINT | Primary Key, Auto Increment |
| content | TEXT | Not Null |
| role | VARCHAR(20) | Not Null |
| created_at | TIMESTAMP | Not Null, Default = current time |
| conversation_id | BIGINT | Foreign Key → conversations.id, Not Null |

### Relationships
- Many messages belong to one conversation

---

# Logical Schema Diagram

```
+-----------+
|   users   |
+-----------+
| id (PK)   |
| username  |
| email     |
| password_hash |
| role      |
+-----------+
      |
      | 1..N
      |
+----------------+
| conversations  |
+----------------+
| id (PK)        |
| title          |
| created_at     |
| user_id (FK)   |
+----------------+
      |
      | 1..N
      |
+----------------+
|   messages     |
+----------------+
| id (PK)        |
| content        |
| role           |
| created_at     |
| conversation_id|
+----------------+
```

---

# JPA Mapping Notes

From your entities:

**User → Conversation**
- @OneToMany(mappedBy = "user", cascade = ALL)

**Conversation → Message**
- @OneToMany(mappedBy = "conversation", cascade = ALL)

**Fetch Type**
- LAZY loading used for relationships (recommended for performance)

**Default Values**
- createdAt is initialized in Java using LocalDateTime.now()

---

# Indexing Recommendations (Next Sprint)

For performance improvement:

Suggested indexes:
- users(username)
- users(email)
- conversations(user_id)
- messages(conversation_id)

---
