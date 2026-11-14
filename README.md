 ## Internal-Hub

A role-based access control (RBAC) system for 
internal HR/identity management, built with pure Java and JDBC.

> ⚠️ **Branch:** `feature/docker-setup` — Implemented containerized environment with Docker, multi-stage Maven builds, and native compatibility for cryptographic libraries (`libc6-compat` / Debian Jammy).

## About
An HR access control system, built with pure JDBC to master the persistence layer that a framework like Spring abstracts away — featuring a native HTTP server architecture and full Docker containerization.
## Tech Stack & Dependencies

- Java 21
- HTTP Server (`com.sun.net.httpserver.HttpServer`) (Native SE module)
- Jackson (`jackson-databind` / `jackson-datatype-jsr310`)
- Argon2id (`de.mkammerer:argon2-jvm`) via JNA
- JWT (`io.jsonwebtoken / com.auth0`)
- MySQL
- JDBC (no ORM)
- JUnit 5
- Docker & Docker Compose

### Authentication (`POST /auth/login`) (Branch Scope)
- Custom route handling with native HttpHandler.
- Input validation and immutability using Java Records (`LoginRequest`).
- Password verification with **Argon2id** algorithm.
- Session token generation with signed JWTs (2-hour expiration).
- DTO mapping and JSON response serialization.
- Centralized error handling with HTTP status codes (`400`, `401`, `404`, `500`) and incident tracking IDs.
- Support for `LocalDate` in JSON serialization.

---

## Authorization
See [AUTHORIZATION.md](./AUTHORIZATION.md) for the full authorization specification.

---

## Docker & Local Environment Setup
The application and its database run fully containerized using Docker Compose.
### 1. Prerequisites
- Docker & Docker Compose installed on your machine.
### 2. Running the Environment
To build the application via multi-stage Maven and start both MySQL and the Java API containers in the background:
```bash
docker compose up --build -d
```
### 3. Useful Commands
View application logs in real-time:
```bash
docker compose logs -f api
```
Quick restart after local Java changes (without full image rebuild):
```bash
mvn clean package -DskipTests
docker compose restart api
```
Stop and remove containers (preserving database volume):
```bash
docker compose down
```
Stop containers and clear database volumes (fresh start):
```bash
docker compose down -v
```
---
## Testing the API
Login Request (cURL)
```bash
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"cbergamaschi@internalhub.com","password":"px6r@W#vWyuv"}'
```
