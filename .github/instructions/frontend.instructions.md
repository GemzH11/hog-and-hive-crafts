---
description: 'Follow React, TypeScript, Vite, and Tailwind conventions used in the frontend app'
applyTo: '**/*.ts, **/*.tsx, **/*.js, **/*.jsx'
---

# Frontend instructions (React + TypeScript)

Treat this file as preferred practice for new and changed frontend code. Existing files may not always match; when touching them, improve alignment without broad unrelated refactors.

## Component and module structure

- Keep page components in `frontend/src/pages` and route wiring in `frontend/src/App.tsx`.
- Keep HTTP calls in `frontend/src/api` (use shared API helpers instead of duplicating fetch logic in pages).
- Keep shared domain types in `frontend/src/types`.
- Keep test/mocking concerns in `frontend/src/test` and `frontend/src/mocks`.

## Test boundaries and placement

- For new component/API tests, prefer `frontend/src/test` plus shared test helpers/mocks.
- Existing adjacent `*.test.ts(x)` files in feature folders can remain until touched; do not churn only for relocation.
- Put browser workflow tests in `frontend/e2e/*.spec.ts` and keep shared E2E support in `frontend/e2e/support`.
- Keep unit/integration-style frontend tests focused on component behavior and API integration contracts; reserve full navigation/cross-page flows for Playwright.

## React + TypeScript patterns

- Use function components and hooks.
- Type state, function inputs, and API responses explicitly; avoid `any`.
- Keep side effects in `useEffect`; handle loading/error state explicitly for async flows.
- Validate/normalize user input close to where it is captured.
- Prefer small helper functions for repeated async flows (load/create/update/delete + reload patterns).

## Styling and UI behavior

- Use Tailwind utility classes directly in JSX, matching existing class composition style.
- Preserve existing accessibility and semantics: labels/placeholders, button types, heading levels, link navigation.
- Keep navigation in `react-router-dom` (`Link`, `useNavigate`, `Routes`).

## Error handling

- Surface actionable errors to users and preserve useful fallback messaging for unknown failures.
- Do not swallow failed promises silently.
- Use logging for developer diagnostics only; keep logs concise and avoid noisy `console` output in shipped code.
- Keep API-origin/mocking behavior compatible with `apiClient` and environment flags.

## Self-explanatory code and comments

- Prefer clear names and straightforward control flow over explanatory comments.
- Add comments only for non-obvious **why** decisions (for example API/mock constraints or router edge cases), not for obvious **what** statements.
