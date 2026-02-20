-- Create users table for Google-authenticated users
CREATE TABLE IF NOT EXISTS app_user (
  id           UUID PRIMARY KEY,
  google_sub   VARCHAR(64) NOT NULL UNIQUE,
  email        VARCHAR(255) NOT NULL,
  name         VARCHAR(255),
  picture_url  TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Helpful index for lookups
CREATE INDEX IF NOT EXISTS idx_app_user_email ON app_user(email);