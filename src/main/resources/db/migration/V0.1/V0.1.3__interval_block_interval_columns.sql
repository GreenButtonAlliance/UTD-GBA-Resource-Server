ALTER TABLE usage.interval_block ADD COLUMN IF NOT EXISTS start BIGINT;
ALTER TABLE usage.interval_block ADD COLUMN IF NOT EXISTS duration INTEGER;
