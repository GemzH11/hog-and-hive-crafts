# Database Schema Docs

This directory contains **documentation** for the Hog & Hive Crafts database schema.

- The **source of truth for the actual database structure** is the Flyway migrations in the repository.
- The **DBML** in this folder is a human-friendly diagram/documentation format that should be kept in sync with the migrations.

## Files in this folder
- `schema.dbml`: DBML source for the database diagram
- `schema.png`: Exported image of the database diagram (generated from DBML).

## How to update the schema docs
1. Make schema changes in the Flyway migrations first (or in parallel).
1. Update `schema.dbml` so it matches the latest migrations.
1. Regenerate the diagram image from `schema.dbml` using a tool such as `https://dbdiagram.io/`
1. Commit both the updated `schema.dbml` and the updated image with the migration changes

## Notes and conventions
- Primary keys are named `id` and use `UUID`, and default to `gen_random_uuid()`
- Foreign keys use the `<table>-id` convention (e.g. `user_id`)
- Most string columns use `text` unless their length needs to specifically be restricted
- Timestamps use `timestamptz` and default to `now()`
