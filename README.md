 ## Internal-Hub

A role-based access control (RBAC) system for 
internal HR/identity management, built with pure Java and JDBC.

> ⚠️ **Branch:** `feature/test-native-httpserver` — Implemented native HTTP server architecture and authentication flow without Spring framework dependencies. Work in progress.

## About
An HR access control system, built with pure JDBC to master the persistence layer that a framework like Spring abstracts away — with a planned migration to Spring as a later phase.
## Tech Stack & Dependencies

- Java 21
- HTTP Server (`com.sun.net.httpserver.HttpServer`) (Native SE module)
- Jackson (`jackson-databind` / `jackson-datatype-jsr310`)
- Argon2id (`de.mkammerer:argon2-jvm`)
- MySQL
- JDBC (no ORM)
- JUnit 5

### Authentication (`POST /auth/login`) (Branch Scope)
- Route handling with native `HttpHandler`.
- Input validation using Java Records (`LoginRequest`).
- Password verification with **Argon2id** algorithm.
- DTO mapping and JSON response serialization.
- Centralized error handling with HTTP status codes (`400`, `401`, `404`, `500`) and incident tracking IDs.
- Support for LocalDate in JSON serialization.

---

## Authorization
See [AUTHORIZATION.md](./AUTHORIZATION.md) for the full authorization specification.

---

## Testing the API

### Login Request
```bash
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"cbergamaschi@internalhub.com","password":"px6r@W#vWyuv"}'
