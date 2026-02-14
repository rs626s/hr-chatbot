
# PostgreSQL Docker Configuration Plan

## Service Overview

Service Name: postgres  
Image: postgres:16  
Purpose: Database for users, conversations, and messages in the HR Chatbot system.

---

## Configuration

### Environment Variables

Required variables (from `.env` file):

- POSTGRES_DB=chatbotdb
- POSTGRES_USER=chatbot_user
- POSTGRES_PASSWORD=<secure_password>

Optional:

- POSTGRES_INITDB_ARGS=--encoding=UTF-8

These values are injected using Docker Compose environment variables.

---

## Port Mapping

Container Port: 5432  
Host Port: 5432

Mapping:
```
5432:5432
```

Port 5432 is the default PostgreSQL port.

---

## Volume Mounting

Purpose: Persist database data across container restarts.

Configuration:

- Named volume: postgres_data
- Mount point: /var/lib/postgresql/data
- Type: Docker-managed volume

Benefits:

- Data survives container restart or deletion
- Easier backups
- Better performance than bind mounts

---

## Initialization Strategy

PostgreSQL automatically executes scripts placed in:

```
/docker-entrypoint-initdb.d/
```

Implementation:

1. Create folder:
```
init-scripts/
```

2. Create file:
```
init-scripts/init.sql
```

3. Mount in docker-compose:

```
- ./init-scripts:/docker-entrypoint-initdb.d
```

---

## Example init.sql

```sql
GRANT ALL PRIVILEGES ON DATABASE chatbotdb TO chatbot_user;
```

Notes:

- Tables are created automatically by Spring Boot (JPA).
- SQL scripts are used for permissions, extensions, or seed data.

---

## Health Check Configuration

Purpose: Ensure PostgreSQL is ready before Spring Boot starts.

Command used:

```
pg_isready -U chatbot_user -d chatbotdb
```

Recommended configuration:

- Interval: 10s
- Timeout: 5s
- Retries: 5
- Start period: 30s

This prevents application startup failures due to database unavailability.

---

## Connection Details

### From Spring Boot Container

Host: postgres  
Port: 5432  
Database: chatbotdb  
Username: chatbot_user  
Password: from environment variable

Example JDBC URL:

```
jdbc:postgresql://postgres:5432/chatbotdb
```

---

### From Local Machine (Development)

Host: localhost  
Port: 5432

Example:

```
jdbc:postgresql://localhost:5432/chatbotdb
```

---

## Backup Strategy

Recommended approaches:

### Volume Backup
Backup Docker volume:

```
docker run --rm -v postgres_data:/volume -v $(pwd):/backup alpine tar czf /backup/postgres_backup.tar.gz /volume
```

### Logical Backup (pg_dump)

```
docker exec chatbot-postgres pg_dump -U chatbot_user chatbotdb > backup.sql
```

Restore:

```
cat backup.sql | docker exec -i chatbot-postgres psql -U chatbot_user -d chatbotdb
```

---

## Future Improvements

Planned enhancements:

- Flyway database migrations
- Index tuning
- Scheduled backups
- Read replica support (advanced deployments)

---

## Summary

PostgreSQL container provides:

- Persistent storage
- Reliable health checks
- Automatic schema generation via JPA
- Easy backup and restore
- Seamless integration with Docker Compose network
