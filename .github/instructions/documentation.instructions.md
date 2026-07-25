---
description: 'Keep README and documentation aligned with behavior, APIs, setup, and developer workflows'
applyTo: '**/*.md, **/*.java, **/*.ts, **/*.tsx, **/*.js, **/*.jsx, **/*.yml, **/*.yaml'
---

# Documentation update instructions

## Update docs when behavior changes

Update `README.md` and/or files under `documentation/` whenever changes affect:

- user-facing features or limitations
- API endpoints, request/response shapes, status codes, or auth expectations
- setup/run/test commands or required tool versions
- configuration keys, environment variables, or default values
- developer workflow steps (build, lint, test, CI expectations)

## Keep examples accurate

- Ensure documented commands and snippets match the current codebase and scripts.
- When code signatures or payload shapes change, update all related examples in the same PR.
- Prefer short, copy-pastable examples.

## Keep documentation concise and navigable

- Extend existing sections before adding new files.
- Use clear headings and task-oriented sections.
- Record only verified behavior; do not document speculative future behavior as current functionality.

## Changelog and release notes

- If a changelog/release note file is present and used in the current scope, add a concise entry for user-visible changes.
- If no changelog is maintained for that area, do not create one solely for minor internal refactors.
