---
description: 'Follow Java and Spring Boot conventions used in this backend codebase'
applyTo: '**/*.java'
---

# Java + Spring backend instructions

Treat this file as best-practice guidance for new and modified backend code. If existing code differs, align touched code toward these conventions without broad unrelated rewrites.

## Core conventions

- Use constructor injection for required dependencies (`private final` fields; Lombok `@RequiredArgsConstructor` is acceptable).
- Keep controller methods thin; delegate business rules to services.
- Keep service methods focused and deterministic; prefer explicit domain exceptions over generic runtime failures.
- Use repository interfaces for persistence access (`JpaRepository`), not manual SQL concatenation.
- Use bean validation (`jakarta.validation`) on request models and rely on centralized exception handling for consistent error responses.

## Error handling and logging

- Fail fast on invalid input and edge cases.
- Throw meaningful domain exceptions (for example `*NotFoundException`) and let global exception handling map them to HTTP responses.
- Use structured SLF4J logging (`log.info("... {}", value)`) instead of `System.out.println` for server diagnostics.
- Prefer user-safe, actionable error messages in API responses; keep internal details in logs.
- Never log secrets or sensitive user data.

## Self-explanatory code and comments

- Prefer clear names and small methods so most code needs no comments.
- Write comments only when they explain **why** a choice exists (trade-off, external constraint, non-obvious behavior).
- Remove stale or redundant comments that restate the code.

## Build and verification

- Run the narrowest relevant checks first, then broader checks if needed.
- For backend changes, use Maven commands from `backend/`.

| Purpose | Command |
| --- | --- |
| Run app | `./mvnw spring-boot:run` |
| Build jar | `./mvnw package` |
| Unit tests | `./mvnw test -Punit` |
| Integration tests | `./mvnw verify -Pintegration` |
| Checkstyle | `./mvnw checkstyle:check` |
| Full verify | `./mvnw verify` |
