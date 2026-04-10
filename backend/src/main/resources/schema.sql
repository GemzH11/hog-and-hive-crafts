CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE "users" (
    "id" UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "email" TEXT,
    "display_name" TEXT,
    "avatar_url" TEXT,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE "patterns" (
    "id" UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "user_id" UUID NOT NULL,
    "name" TEXT NOT NULL,
    "source" TEXT,
    "craft_type" TEXT,
    "notes" TEXT,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE "files" (
    "id" UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "pattern_id" UUID NOT NULL,
    "role" TEXT NOT NULL,
    "display_name" TEXT,
    "storage_path" TEXT NOT NULL,
    "description" TEXT,
    "content_type" TEXT,
    "size_bytes" BIGINT,
    "checksum_sha256" TEXT,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX "uq_patterns_user_id_name" ON "patterns" ("user_id", "name");

ALTER TABLE "patterns" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE;
ALTER TABLE "files" ADD FOREIGN KEY ("pattern_id") REFERENCES "patterns" ("id") ON DELETE CASCADE;