CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE capture_items (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title VARCHAR(255) NOT NULL,
  text TEXT,
  type VARCHAR(50) NOT NULL,
  language VARCHAR(50),
  done BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_capture_items_type
    CHECK (type IN ('TASK','REMEMBER','SNIPPET','IDEA')),
  CONSTRAINT chk_done_only_for_task
    CHECK (done = FALSE OR type = 'TASK')
);

CREATE INDEX idx_capture_items_type ON capture_items(type);
CREATE INDEX idx_capture_items_done ON capture_items(done);
CREATE INDEX idx_capture_items_created_at ON capture_items(created_at);