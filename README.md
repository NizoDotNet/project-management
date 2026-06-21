# Project Management Tool

A self-hosted, Jira-like project management platform built with **Spring Boot** and **Keycloak**. Built as a learning project for transitioning from .NET to Java/Spring.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4.x |
| Database | PostgreSQL 17 |
| Migrations | Flyway |
| Auth | Keycloak (OAuth2 / OpenID Connect) |
| ORM | Spring Data JPA / Hibernate |
| API Docs | springdoc-openapi (Swagger UI) |
| Caching | Redis |
| Real-time | Spring WebSocket |
| Mapping | MapStruct |
| Build Tool | Maven |

---

## Features

- Organizations, teams, and team membership with roles
- Projects with members, owners, and status tracking
- Kanban / Scrum boards with custom columns
- Sprints and milestones
- Tickets with assignees, comments (threaded), attachments, and full change history
- Labels for tagging tickets
- In-app notifications with per-user preferences
- Keycloak-backed authentication and role-based authorization
- Swagger UI with OAuth2 login for API testing

---

## Architecture

```
project-management/
├── src/main/java/com/prjmng/
│   ├── config/          # Security, Swagger, WebSocket, Redis config
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic
│   ├── repository/       # Spring Data JPA repositories
│   ├── entity/            # JPA entities
│   ├── dto/                # Request/response DTOs
│   ├── mapper/            # MapStruct mappers
│   ├── enums/              # Domain enums (status, role, type, priority)
│   ├── security/           # JWT claims helpers, SecurityUtils
│   └── exception/          # Global exception handling
└── src/main/resources/
    ├── application.yml     # App configuration
    └── db/migration/       # Flyway SQL migrations
```

---

## Prerequisites

| Tool | Version |
|---|---|
| JDK | 21 (LTS) |
| Maven | 3.9+ |
| Docker | Latest |
| Docker Compose | Latest |

---

## Getting Started

### 1. Clone and start infrastructure

```bash
git clone <your-repo-url>
cd project-management
docker compose up -d
```

This starts:
- **PostgreSQL** on `localhost:5431`
- **Keycloak** on `localhost:8180`
- **Redis** on `localhost:6379`

### 2. Configure Keycloak

1. Open `http://localhost:8180` and log in with `admin / admin`
2. Create a realm: `project-management`
3. Create a client: `pm-app`
    - Client type: OpenID Connect
    - Valid redirect URIs:
        - `http://localhost:8080/swagger-ui/**`
        - `http://localhost:8080/swagger-ui/oauth2-redirect.html`
    - Web origins: `http://localhost:8080`
4. Create at least one test user under **Users**

### 3. Run the application

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

### 4. Verify

| Resource | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API Docs (JSON) | http://localhost:8080/v3/api-docs |
| Health check | http://localhost:8080/actuator/health |
| Keycloak admin | http://localhost:8180 |

---

## Database Migrations

Migrations live in `src/main/resources/db/migration` and run automatically on startup via Flyway.

**Rule:** never edit a migration file once it has been applied. Always add a new `V{n}__description.sql` file instead.

```
db/migration/
└── V1__init_schema.sql
```

---

## Configuration

Key values in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5431/prjmng
    username: postgres
    password: postgres

  flyway:
    enabled: true
    locations: classpath:db/migration

keycloak:
  auth-server-url: http://localhost:8180
  realm: project-management
  resource: pm-app
```

---

## Domain Model

The core entity relationships:

```
Organization
 └── Team ── TeamMember ── User
 └── Project ── ProjectMember ── User
      └── Board
           ├── BoardColumn
           ├── Sprint
           └── Ticket
                ├── TicketAssignee
                ├── TicketComment (threaded)
                ├── TicketAttachment
                ├── TicketHistory
                └── TicketLabel ── Label

User ── Notification
User ── NotificationSetting
```

See `/docs/erd.md` (or the ER diagram shared during planning) for the full schema with field-level detail.

---

## Roadmap

- [x] Project setup, Docker infrastructure
- [x] Keycloak authentication
- [x] Database schema and entities
- [ ] Repository layer
- [ ] Service layer with business rules
- [ ] REST controllers
- [ ] WebSocket real-time ticket updates
- [ ] Email notifications
- [ ] Frontend client

---

## License

Personal learning project — no license specified yet.