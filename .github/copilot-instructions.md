# Hog & Hive Crafts Copilot Instructions

Use this file as the entry point. Keep responses in **English** and keep review feedback **specific, constructive, and actionable**.

## Project overview

Hog & Hive Crafts is a full-stack web app for organizing craft patterns and related project data.

| Area | Stack / Tools |
| --- | --- |
| Backend | Java, Spring Boot, Maven, Spring Security, Spring Data JPA |
| Database | PostgreSQL, Flyway |
| Backend testing | JUnit, Mockito, Testcontainers |
| Frontend | React, TypeScript, Vite, Tailwind CSS |
| Frontend testing | React Testing Library, Vitest, Playwright |
| DevOps & tooling | Git, GitHub, Docker, Render |

## How this repository is organized

- `backend/` - Spring Boot API (controller/service/repository, DTOs, mappers, exceptions, config)
- `frontend/` - React + TypeScript app (pages, API client, mocks, tests)
- `documentation/` - project docs

## Use domain-specific instruction files

Follow the focused rules in `.github/instructions/`:

- `architecture.instructions.md` - cross-cutting architecture and layering
- `java-spring.instructions.md` - Java/Spring backend rules
- `frontend.instructions.md` - React/TypeScript/Tailwind frontend rules
- `testing.instructions.md` - backend + frontend testing conventions
- `documentation.instructions.md` - when/how to update docs and README
- `markdown.instructions.md` - Markdown style and accessibility

Do not duplicate those rules here; apply the file that matches the files being changed.
