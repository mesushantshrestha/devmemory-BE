-- 1) Add nullable user_id first (safe for existing rows)
ALTER TABLE items
  ADD COLUMN IF NOT EXISTS user_id UUID;

-- 2) Add foreign key constraint
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE constraint_name = 'fk_items_user'
      AND table_name = 'capture_items'
  ) THEN
    ALTER TABLE capture_items
      ADD CONSTRAINT fk_items_user
      FOREIGN KEY (user_id) REFERENCES app_user(id)
      ON DELETE CASCADE;
  END IF;
END $$;

-- 3) Add index for fast per-user queries
CREATE INDEX IF NOT EXISTS idx_items_user_id ON items(user_id);