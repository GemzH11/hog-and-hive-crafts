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

## Dependency upgrades (Dependabot and automated remediation)

When fixing a failing Dependabot PR or handling a dependency bump:

- Read the release notes and migration guide, and change only what the upgrade requires.
- Keep architecture, testing, and documentation conventions from the instruction files above.
- Backend (from `backend/`): run `./mvnw checkstyle:check`, `./mvnw test -Punit`, and `./mvnw verify -Pintegration` (Docker is required for Testcontainers).
- Frontend (from `frontend/`): run `npm run lint`, `npm run test:unit`, and `npm run build`.
- Never disable or delete tests, relax lint rules, or lower coverage thresholds to make a build pass.
- Do not downgrade or pin the dependency unless the upgrade cannot be made to work; explain why in the PR.
- Update `README.md` or `documentation/` if versions, setup, or behavior change.
- Major updates always need human review; summarize breaking changes in the PR.
