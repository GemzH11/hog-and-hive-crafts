---
description: 'Write accessible, well-structured markdown with clear links, headings, and plain language'
applyTo: '**/*.md'
---

# Markdown accessibility and style

## 1) Descriptive links

- Avoid generic link text like "here" or "click here".
- Make link text understandable out of context.
- Avoid using the same link text for different destinations.
- Prefer descriptive links over bare URLs in prose.

## 2) Alt text for images

- Add meaningful alt text for non-decorative images.
- Avoid filename-like alt text (`image123.png`) or placeholder text (`image`, `screenshot`).
- Include important visible text from screenshots/charts in the description.

## 3) Heading hierarchy

- Use one H1 per document.
- Do not skip heading levels (`##` -> `####`).
- Use real headings instead of bold text as pseudo-headings.

## 4) Plain language

- Prefer short sentences and common words.
- Break dense paragraphs into lists/sections.
- Write navigation steps as explicit text actions; do not rely only on icon descriptions.

## 5) Lists and emoji

- Use proper markdown list syntax (`-`, `*`, `1.`), not emoji bullets.
- Use emoji sparingly and never as the only carrier of meaning.

## Review priority

1. Missing/poor alt text
2. Heading hierarchy issues
3. Non-descriptive links
4. Improper list structure
5. Plain-language improvements
