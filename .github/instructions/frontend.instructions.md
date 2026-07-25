---
description: 'Follow React, TypeScript, Vite, and Tailwind conventions used in the frontend app'
applyTo: '**/*.ts, **/*.tsx, **/*.js, **/*.jsx'
---

# Frontend instructions (React + TypeScript)

## Component and module structure

- Keep page components in `frontend/src/pages` and route wiring in `frontend/src/App.tsx`.
- Keep HTTP calls in `frontend/src/api` (use shared API helpers instead of duplicating fetch logic in pages).
- Keep shared domain types in `frontend/src/types`.
- Keep test/mocking concerns in `frontend/src/test` and `frontend/src/mocks`.

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

- Surface actionable errors to users (existing pattern: `err instanceof Error ? err.message : "Unknown error"`).
- Do not swallow failed promises silently.
- Keep API-origin/mocking behavior compatible with `apiClient` and environment flags.

## Self-explanatory code and comments

- Prefer clear names and straightforward control flow over explanatory comments.
- Add comments only for non-obvious **why** decisions (for example API/mock constraints or router edge cases), not for obvious **what** statements.
