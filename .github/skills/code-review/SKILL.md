# Code Review Skill

Conduct thorough, constructive code reviews for the Hog & Hive Crafts repository using consistent architectural, language, testing, and documentation standards.

## Overview

This skill enables you to review pull requests against the repository's established conventions. It covers **architecture and layering**, **backend and frontend language guidelines**, **testing practices**, **documentation updates**, and **accessibility standards**.

All guidance is documented in the repository's instruction files under `.github/instructions/`. This skill synthesizes those standards into a structured review workflow.

## When to use

- Reviewing a pull request for architectural correctness, code quality, and adherence to conventions
- Checking backend (Java + Spring) or frontend (React + TypeScript) code changes
- Validating test coverage and testing patterns
- Ensuring documentation stays synchronized with code changes
- Providing specific, actionable feedback grounded in repository standards

## Core workflow

1. **Check architecture and layering first** — if layer boundaries are violated, other improvements may not stick
2. **Review language-specific conventions** — Java/Spring for backend; React/TypeScript for frontend
3. **Validate testing coverage** — unit, integration, and E2E tests as appropriate
4. **Confirm documentation is updated** — especially if behavior, APIs, or workflows changed
5. **Polish style and accessibility** — markdown accessibility, type safety, semantic HTML

## Key review areas

### Architecture & Layering

- Controllers stay thin (HTTP mapping, validation); business logic goes to services
- Services are stateless, constructor-injected, and called by controllers
- Repositories handle only persistence (JpaRepository interfaces)
- DTOs are API contracts; entities are not returned directly from endpoints
- Exceptions are thrown by services and caught globally via centralized handlers

### Backend (Java + Spring)

- Constructor injection with `private final` fields; accept Lombok `@RequiredArgsConstructor`
- Error handling: throw meaningful domain exceptions; use SLF4J structured logging
- Self-explanatory code: comments explain **why**, not **what**
- Verify: tests pass, checkstyle passes, Maven `verify` succeeds

### Frontend (React + TypeScript)

- Function components and hooks; explicit type safety (avoid `any`)
- Shared HTTP logic in `frontend/src/api`; duplicate fetch calls consolidated
- Async state handled explicitly (loading, error, success)
- Accessibility: semantic HTML, labels/placeholders, link navigation via `react-router-dom`
- MSW mocks for API responses in tests

### Testing

- **Backend:** Unit tests (`*Test.java`) use mocks; integration tests (`*IT.java`) use Testcontainers
- **Frontend:** Component tests (`*.test.ts(x)`) use Vitest + RTL; E2E tests (`*.spec.ts`) use Playwright
- Error paths covered, not only happy paths
- Descriptive test names; Arrange-Act-Assert structure

### Documentation

- Update `README.md` and `documentation/` when behavior, APIs, commands, or workflows change
- Ensure examples match current codebase
- Markdown accessibility: descriptive links, meaningful alt text, proper heading hierarchy

## Review checklist

Before approving, confirm:

- ✅ Architecture: Layering is correct; dependencies flow correctly
- ✅ Naming and clarity: Code is self-explanatory; comments explain **why**
- ✅ Error handling: Exceptions are meaningful; no swallowed errors
- ✅ Type safety (frontend): No `any` without reason; async state handled
- ✅ Testing: Critical paths covered; at least one error case tested
- ✅ Accessibility: Semantic HTML, descriptive links, meaningful alt text
- ✅ Documentation: README/docs match behavior changes; examples are current
- ✅ Build passes: Tests, linting, verification pass
- ✅ Scope: Changes are cohesive; no unnecessary cross-layer refactors

## Common anti-patterns to catch

| Layer | Anti-pattern | Fix |
| --- | --- | --- |
| Backend | Repository injected into controller | Move logic to service; call service from controller |
| Backend | JPA entity returned from endpoint | Map to DTO via service or mapper |
| Backend | `@Autowired` field injection | Use constructor injection with `private final` |
| Backend | Generic `RuntimeException` thrown | Throw domain exception (`*NotFoundException`, etc.) |
| Frontend | `any` type in TypeScript | Define concrete type or narrow `unknown` |
| Frontend | Fetch call duplicated in multiple pages | Move to `frontend/src/api` helper |
| Frontend | Unhandled async state | Add loading/error state and render appropriately |
| Frontend | `<div role="button">` | Use `<button>` element |
| Frontend | `<a>` for internal routes | Use `react-router-dom` `<Link>` or `useNavigate` |

## How to provide feedback

- **Be specific**: Link the issue to code, not abstract critique
- **Be constructive**: Explain **why** the change matters; suggest alternatives
- **Reference standards**: Link to `.github/instructions/` files to ground feedback
- **Acknowledge context**: If there's a design decision or constraint, mention it

**Example:**

❌ "This doesn't follow the architecture"

✅ "This controller calls the repository directly (line 42). Per the architecture guidelines, business logic belongs in the service layer. Move the query to `PatternService.findPatternsByTag()` and call that from the controller. See `.github/instructions/architecture.instructions.md` for details."

## Instruction files

This skill references the repository's instruction files:

- [`.github/instructions/architecture.instructions.md`](../../instructions/architecture.instructions.md) — Backend and frontend layering conventions
- [`.github/instructions/java-spring.instructions.md`](../../instructions/java-spring.instructions.md) — Java + Spring backend conventions
- [`.github/instructions/frontend.instructions.md`](../../instructions/frontend.instructions.md) — React + TypeScript frontend conventions
- [`.github/instructions/testing.instructions.md`](../../instructions/testing.instructions.md) — Testing conventions for backend and frontend
- [`.github/instructions/documentation.instructions.md`](../../instructions/documentation.instructions.md) — Documentation update guidelines
- [`.github/instructions/markdown.instructions.md`](../../instructions/markdown.instructions.md) — Markdown accessibility standards
- [`.github/instructions/code-review.instructions.md`](../../instructions/code-review.instructions.md) — Comprehensive code review guide

## Tips for effective reviews

1. **Start with architecture**: Fix layering issues first; style improvements come after.
2. **Link to patterns**: When you see an issue, reference the relevant instruction file or example from the codebase.
3. **Prioritize clarity**: Self-explanatory code and clear naming prevent most bugs; style is secondary.
4. **Test coverage matters**: Verify critical paths and at least one error case are tested.
5. **Keep scope manageable**: If a PR touches multiple layers or introduces new patterns, ask why before approving.
6. **Acknowledge trade-offs**: Some constraints (performance, legacy code, library limitations) are valid; mention them if they justify deviation.

## Escalation

If you encounter:

- **Architecture disagreement** → Escalate to maintainers; link to `architecture.instructions.md`
- **Ambiguous testing requirement** → Suggest specific test cases (error paths, edge cases)
- **Incomplete documentation** → List specific sections needing updates and why
- **Performance concern** → Explain the issue with metrics; propose a solution or defer to profiling

---

**Last updated**: October 2026  
**Repository**: [GemzH11/hog-and-hive-crafts](https://github.com/GemzH11/hog-and-hive-crafts)
