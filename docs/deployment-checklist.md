# Deployment Checklist

HR Chatbot -- Production Deployment

## Pre-Deployment

-   Docker installed
-   Java 17+ installed
-   Maven installed
-   .env file created
-   Secrets configured securely

------------------------------------------------------------------------

## Database

-   docker-compose.yml verified
-   PostgreSQL container running
-   Healthcheck status: healthy
-   Flyway migrations validated

------------------------------------------------------------------------

## Application

-   Production profile active
-   SPRING_PROFILES_ACTIVE=prod
-   Application starts without errors
-   Actuator health endpoint accessible

------------------------------------------------------------------------

## Security

-   Strong DB password
-   Strong JWT secret
-   .env not committed to Git
-   Database port restricted (if cloud deployed)

------------------------------------------------------------------------

## Verification

-   Register test user
-   Confirm user stored in PostgreSQL
-   Test login and JWT generation
-   Validate Flyway schema history table exists

Deployment Ready: YES / NO
