---
description: 'Code review skill for Hog & Hive Crafts: apply architecture, language, testing, and documentation standards'
applyTo: '**'
---

# Code Review instructions

Use this guide to conduct consistent, actionable code reviews on Hog & Hive Crafts pull requests. Reviews should be **specific, constructive, and tied to repository conventions**.

## Review flow and priorities

1. **Architecture & layering** (cross-cutting concern, highest priority)
2. **Language conventions** (backend Java/Spring or frontend React/TypeScript)
3. **Testing coverage and quality**
4. **Documentation updates** (if behavior changed)
5. **Style, accessibility, and clarity**

---

## Architecture & layering review

Apply guidance from `architecture.instructions.md` first—if layering is wrong, other improvements may not stick.

### Backend flow and dependency direction

- Controllers stay thin (HTTP mapping, validation); business logic goes to services.
- Services are stateless, constructor-injected, and called by controllers (no direct controller→repository calls).
- Repositories handle only persistence access (JpaRepository interfaces).
- DTOs are API contracts; services return DTOs via mappers when needed.
- Exceptions cross layers (thrown by service, caught globally, mapped to HTTP responses).

**Common issues:**
- Repository injected directly into controller → move logic to service layer.
- JPA entity returned directly from controller → create/map a DTO.
- Business logic in controller or repository → belongs in service.
- Service with mutable state or side effects outside method scope → refactor to stateless operations.

### Frontend structure

- Route components live in `frontend/src/pages` and are wired in `frontend/src/App.tsx`.
- Reusable UI components go in `frontend/src/components`.
- HTTP calls are in `frontend/src/api`; avoid duplicating fetch logic in pages.
- Shared types are in `frontend/src/types`.
- Test/mock helpers belong in `frontend/src/test` or `frontend/src/mocks`.

**Common issues:**
- API calls scattered across multiple pages → consolidate in `frontend/src/api`.
- Shared types defined in multiple places → move to `frontend/src/types` and import.
- Test files in feature folders instead of centralized location → acceptable if files exist; refactor only if task touches them.

### Change scope

- New code fits the closest existing package/module matching its responsibility.
- Prefer extending existing patterns over introducing new architectural styles.
- Avoid cross-layer refactors unless required by the task.

---

## Backend (Java + Spring) conventions

Apply guidance from `java-spring.instructions.md` to backend changes.

### Dependency injection and structure

- Use constructor injection with `private final` fields; accept Lombok `@RequiredArgsConstructor`.
- Required dependencies are constructor parameters; optional dependencies use setter injection (rare).
- Services have no mutable state; keep them focused and deterministic.

**Red flags:**
- `@Autowired` field injection instead of constructor injection.
- Service holds state across requests (non-thread-safe).
- Methods with side effects not reflected in their signature.

### Error handling and logging

- Throw meaningful domain exceptions (`*NotFoundException`, custom checked exceptions); let global handlers map them to HTTP.
- Use structured SLF4J logging: `log.info("... {}", value)` instead of string concatenation or `System.out.println`.
- Error messages in API responses are user-safe and actionable; internal details stay in logs.
- Never log secrets, tokens, or sensitive user data.

**Check:**
- Validation errors are caught early and mapped to HTTP 400/422.
- Not-found cases throw `*NotFoundException` (or similar), not generic runtime exceptions.
- Error responses are consistent (check `backend/.../exception` handlers).

### Self-explanatory code

- Prefer clear class/method/variable names and small focused methods.
- Comments explain **why** (trade-off, constraint, non-obvious behavior), not **what**.
- Remove stale or redundant comments that restate code.

**Ask during review:**
- "Can this method be smaller?" → split into helper methods.
- "Is the name clear?" → rename or add a comment.
- "Does this comment restate the code?" → remove it or explain the **why**.

### Build and verification

For backend changes, run (from `backend/`):

| Purpose | Command |
| --- | --- |
| Unit tests | `./mvnw test -Punit` |
| Integration tests | `./mvnw verify -Pintegration` |
| Checkstyle | `./mvnw checkstyle:check` |
| Full verify | `./mvnw verify` |

**In review:**
- Confirm PR includes relevant tests (unit or integration).
- Checkstyle should pass (no trailing whitespace, import order, etc.).

---

## Frontend (React + TypeScript) conventions

Apply guidance from `frontend.instructions.md` to frontend changes.

### Component and type safety

- Use function components and hooks.
- Type state, function inputs, and API responses explicitly; avoid `any`.
- Side effects go in `useEffect`; handle loading/error state explicitly.
- Validate/normalize user input close to where it is captured.

**Red flags:**
- `any` types without explanation.
- Unhandled loading or error states in async flows.
- Input validation only at submission (should validate on input/blur).
- Global state mutations outside of intentional state management.

### API integration and helpers

- HTTP calls use shared helpers in `frontend/src/api` (not duplicated fetch logic in pages).
- MSW handlers control API responses in tests.
- Failed promises are not swallowed; errors are logged or surfaced to users.

**Check:**
- No `fetch()` calls scattered in page components; calls go through API helpers.
- Tests use MSW to mock API responses, not hard-coded data.
- Error cases are handled (catch blocks, error state, user messaging).

### Styling and accessibility

- Use Tailwind utility classes directly in JSX, matching existing composition style.
- Preserve accessibility: labels/placeholders, button types (`type="button"` vs `type="submit"`), heading hierarchy, link navigation.
- Navigation uses `react-router-dom` (`Link`, `useNavigate`, `Routes`), not `<a>` tags for internal routes.

**Red flags:**
- `<div role="button">` instead of `<button>`.
- Missing `<label>` for form inputs.
- Heading hierarchy skipped (`<h1>` → `<h3>`).
- `<a href="...">` for internal navigation instead of `<Link>`.

### Self-explanatory code

- Prefer clear names and straightforward control flow.
- Comments explain non-obvious **why** decisions (API/mock constraints, router edge cases), not obvious **what** statements.

---

## Testing conventions

Apply guidance from `testing.instructions.md` to new or modified tests.

### Backend testing (JUnit, Mockito, Testcontainers)

**Scope and naming:**
- Unit tests: `*Test.java` (Mockito/JUnit, fast, no container).
- Integration tests: `*IT.java` (Testcontainers + Spring context, comprehensive but slower).

**Quality checks:**
- Mock external collaborators (repositories, mappers), not the class under test.
- Use Arrange-Act-Assert clearly; descriptive test names state behavior and outcome.
- Each test is independent and deterministic.
- Error paths are covered (validation errors, not-found cases, exception mapping), not only happy paths.
- Verify behavior/interactions only when order or arguments matter; avoid over-verification.

**Integration tests:**
- Reuse existing `AbstractIT` patterns and shared test data helpers.
- Avoid per-test container lifecycle changes; keep setup/teardown stable.

### Frontend testing (RTL, Vitest, Playwright)

**Scope and naming:**
- Component/API tests: `*.test.ts(x)` and `*.unit.test.ts` (Vitest + RTL, preferred under `frontend/src/test` for new tests).
- E2E tests: `frontend/e2e/*.spec.ts` (Playwright).

**Quality checks:**
- Test user-visible behavior and accessible queries (`getByRole`, `findByRole`) before implementation details.
- Await async UI transitions explicitly (`findBy...`, `waitFor`), not timing assumptions.
- Use MSW handlers to control API responses; do not hard-code data in tests.
- Each test is independent; shared helpers go in `frontend/e2e/support`.
- Playwright focused on cross-page/browser workflows, not low-level component behavior.

### Coverage

- Critical paths and behavior changes introduced by the PR have test coverage.
- At least one failure/edge-case test for validation, error handling, or branching logic changes.

---

## Documentation updates

Apply guidance from `documentation.instructions.md` and `markdown.instructions.md`.

### When to update docs

Update `README.md` and/or `documentation/` when changes affect:

- User-facing features or limitations.
- API endpoints, request/response shapes, status codes, or auth expectations.
- Setup/run/test commands or required tool versions.
- Configuration keys, environment variables, or default values.
- Developer workflow steps (build, lint, test, CI expectations).

### Documentation quality

- Ensure documented commands and snippets match current codebase and scripts.
- When code signatures or payload shapes change, update all related examples in the same PR.
- Keep examples short and copy-pastable.
- Document verified current behavior; label planned (unimplemented) behavior explicitly.

### Markdown accessibility

- **Links:** Descriptive text, understandable out of context (avoid "here" or "click here").
- **Images:** Meaningful alt text; avoid filenames or placeholders; include visible text from screenshots.
- **Headings:** One H1 per document; do not skip levels; use real headings, not bold pseudo-headings.
- **Lists:** Proper markdown syntax (`-`, `*`, `1.`), not emoji bullets.
- **Clarity:** Short sentences, common words, explicit navigation steps.

---

## Checklist for reviewers

Before approving a PR, confirm:

- [ ] **Architecture**: Layering is correct (controllers thin, logic in services, persistence in repositories).
- [ ] **Dependency flow**: Services called by controllers, not vice versa; DTOs used when appropriate.
- [ ] **Naming and clarity**: Code is self-explanatory; comments explain **why**, not **what**.
- [ ] **Error handling**: Meaningful exceptions thrown and caught globally; no swallowed errors.
- [ ] **Type safety (frontend)**: No `any` without reason; async state handled explicitly.
- [ ] **Testing**: Tests cover critical paths and at least one error case; naming is descriptive.
- [ ] **Accessibility**: Links/headings/inputs are semantic; alt text is meaningful.
- [ ] **Documentation**: README/docs match behavior changes; examples are current.
- [ ] **Build passes**: Tests, linting, and verification pass for the relevant layer.
- [ ] **Scope**: Changes are cohesive and fit existing patterns; no unnecessary cross-layer refactors.

---

## Style guide for review comments

- **Be specific**: "This controller calls the repository directly" → "Move this query to `PatternService` because business logic belongs in the service layer."
- **Be constructive**: Link to the relevant instruction file or example from the codebase.
- **Avoid gatekeeping**: Focus on clarity, correctness, and maintainability, not personal preference.
- **Acknowledge trade-offs**: If a constraint or design decision exists, mention it: "I see you're using `@Transactional` for rollback semantics; confirm that's intentional."

---

## Common patterns to watch for

### Backend patterns

| Pattern | Issue | Fix |
| --- | --- | --- |
| `@Autowired` fields | Not constructor-injected. | Use `@RequiredArgsConstructor` + `private final`. |
| Service holds state | Thread-unsafe. | Move state to method parameters or request-scoped beans. |
| Controller calls repository | Bypasses service layer. | Add a service method and call it from the controller. |
| Entity returned from endpoint | Exposes internals; violates API contract. | Map to a DTO in the service or mapper. |
| Generic RuntimeException thrown | No domain context. | Throw a custom `*NotFoundException` or validation exception. |
| No test for error case | Happy path only; fragile. | Add a test for validation failure or not-found response. |

### Frontend patterns

| Pattern | Issue | Fix |
| --- | --- | --- |
| `any` type | No type safety. | Define a concrete type or use `unknown` and narrow it. |
| Unhandled loading state | UI flickers or freezes. | Track loading; render a spinner or disable input. |
| Fetch call in component | Logic scattered; hard to test. | Move to `frontend/src/api` helper; import and call it. |
| No MSW mock in test | Test depends on live API. | Add MSW handler to mock the response. |
| `<div role="button">` | Not semantic; accessibility issue. | Use `<button>` element. |
| `<a href="...">` for route | External link style on internal nav. | Use `react-router-dom` `<Link>` or `useNavigate`. |

---

## Escalation and edge cases

- **Disagreement on architecture**: Link to `architecture.instructions.md`; escalate to maintainers if the PR is in a grey area.
- **Testing coverage gaps**: Suggest specific test cases (error paths, edge cases); do not just ask for "more tests."
- **Performance concern**: Explain the issue with metrics or expected impact; propose a solution or defer to profiling.
- **Incomplete docs**: List the specific sections that need updating and why.

---

## Additional resources

- Architecture: `.github/instructions/architecture.instructions.md`
- Backend (Java + Spring): `.github/instructions/java-spring.instructions.md`
- Frontend (React + TypeScript): `.github/instructions/frontend.instructions.md`
- Testing: `.github/instructions/testing.instructions.md`
- Documentation: `.github/instructions/documentation.instructions.md`
- Markdown accessibility: `.github/instructions/markdown.instructions.md`
