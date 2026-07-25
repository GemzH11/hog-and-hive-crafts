---
description: 'Apply repository architecture and layering conventions across backend and frontend changes'
applyTo: '**'
---

# Architecture conventions

## Keep layer boundaries clear

- Keep Spring controllers in `backend/.../controller` focused on HTTP concerns (request mapping, validation, response status).
- Keep business logic in `backend/.../service` classes.
- Keep persistence access in `backend/.../repository` (`JpaRepository` interfaces).
- Keep API contracts in `backend/.../dto`; map entities to DTOs through `backend/.../mapper`.
- Keep cross-cutting HTTP error translation in `backend/.../exception` (for example `GlobalExceptionHandler`).

## Dependency direction

- Prefer one-way flow: `controller -> service -> repository`.
- Do not inject repositories directly into controllers.
- Do not return JPA entities directly from controllers when DTOs already exist for the endpoint.
- Keep services stateless and constructor-injected.

## Frontend structure

- Keep page-level UI in `frontend/src/pages`.
- Keep HTTP and transport logic in `frontend/src/api`.
- Keep shared types in `frontend/src/types`.
- Keep test-only mock infrastructure under `frontend/src/mocks` and `frontend/e2e/support`.

## Scope and change discipline

- Place new code in the closest existing package/module that matches its responsibility.
- Prefer extending existing patterns over introducing new architectural patterns.
- Keep changes small and cohesive; avoid cross-layer refactors unless required by the task.
