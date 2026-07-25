---
description: 'Use repository testing conventions for JUnit/Mockito/Testcontainers and RTL/Vitest/Playwright'
applyTo: '**/*Test.java, **/*IT.java, **/*.test.ts, **/*.test.tsx, **/*.unit.test.ts, **/*.unit.test.tsx, **/*.spec.ts, **/*.spec.tsx'
---

# Testing instructions

## Test file scope and naming

- Backend unit tests: `*Test.java` (Mockito/JUnit).
- Backend integration tests: `*IT.java` (Testcontainers + Spring context).
- Frontend component/API tests: `*.test.ts(x)` and `*.unit.test.ts` (Vitest + Testing Library/MSW).
- Frontend E2E tests: `frontend/e2e/*.spec.ts` (Playwright).

## Structure and readability

- Follow Arrange-Act-Assert (or Given-When-Then) clearly.
- Use descriptive test names that state behavior and expected outcome.
- Keep each test independent and deterministic.
- Prefer specific assertions over broad truthy/falsy assertions.

## Backend guidance (JUnit, Mockito, Testcontainers)

- Mock external collaborators (repositories, mappers, gateways) in unit tests; do not mock the class under test.
- Verify behavior and key interactions when interaction order/arguments matter.
- For integration tests, reuse existing `AbstractIT`/shared test data helpers and avoid per-test container lifecycle changes.
- Cover error paths (validation errors, not-found cases, exception mapping), not only happy paths.

## Frontend guidance (RTL, Vitest, Playwright)

- Test user-visible behavior and accessible queries (`getByRole`, `findByRole`) before implementation details.
- Use MSW handlers to control API responses in unit/integration-style frontend tests.
- Await async UI transitions explicitly (`findBy...`, `waitFor`) instead of timing assumptions.
- In Playwright tests, keep setup/teardown isolated and resilient (existing helpers in `frontend/e2e/support`).

## Coverage expectation

- Add or update tests for critical paths and behavior changes introduced by the PR.
- Include at least one failure/edge-case test when changing validation, error handling, or branching logic.
