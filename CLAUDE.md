# CLAUDE.md

## Project Overview

**Parking System** — A platform that allows employees to request access to company parking spots, with management-level validation of those requests. If no management responds within the configured window, the first requester is automatically assigned the spot.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security 6, Spring Data JPA |
| Database | PostgreSQL 16, Flyway migrations |
| Frontend | Vue 3, TypeScript, Vite, Pinia, Vue Router 4 |
| Styling | Tailwind CSS, SCSS |
| Auth | JWT (stateless) + refresh tokens |
| API Docs | SpringDoc OpenAPI (Swagger UI at `/swagger-ui.html`) |

## Commands

### Backend
```bash
# Run with Docker Compose (recommended)
docker-compose up

# Run backend only (requires local PostgreSQL)
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run tests
cd backend
mvn test

# Build JAR
cd backend
mvn clean package -DskipTests
```

### Frontend
```bash
cd frontend
npm install
npm run dev       # dev server at http://localhost:5173
npm run build     # production build
npm run test      # Vitest unit tests
npm run lint      # ESLint
```

### Docker Compose
```bash
docker-compose up -d          # start all services
docker-compose down           # stop all services
docker-compose logs backend   # backend logs
```

## Architecture

```
parking-system/
├── backend/          Java Spring Boot REST API
├── frontend/         Vue 3 SPA
└── docker-compose.yml
```

### Role / Level Mapping

| Level | Role |
|---|---|
| ≤ 9 (descending) | MANAGEMENT — can approve/reject requests |
| 10–13 | EMPLOYEE — can request parking spots |
| (none) | ADMIN — full CRUD over spots, users, and audit logs |

### Request Lifecycle

```
PENDING → APPROVED / REJECTED  (by management)
PENDING → AUTO_APPROVED         (after auto_approve_deadline passes with no response)
PENDING → CANCELLED             (by the requesting employee)
```

On approval or auto-approval, a `ParkingAssignment` record is created and the spot status flips to `OCCUPIED`.

### Key Conventions

- **UUID primary keys** — `gen_random_uuid()` default in PostgreSQL, `@GeneratedValue(strategy = GenerationType.UUID)` in JPA
- **Soft deletes** — `is_active = false` instead of `DELETE`; spots and users are never hard-deleted
- **Flyway migrations** — all schema changes go in `backend/src/main/resources/db/migration/`; Hibernate DDL is set to `validate`
- **DTO layer** — controllers never return entities directly; always use request/response DTOs
- **Enums as VARCHAR** — stored as strings in PostgreSQL for easy migration extensibility
- **Optimistic locking** — `@Version` on `ParkingSpot` as a second layer after `SELECT FOR UPDATE`
- **Auto-approval scheduler** — runs every 15 minutes via `@Scheduled`; configurable window in `application.yml`
