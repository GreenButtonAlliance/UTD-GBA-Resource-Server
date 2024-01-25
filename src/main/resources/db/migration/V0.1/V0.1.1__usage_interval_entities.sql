DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'quality_of_reading' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.quality_of_reading AS ENUM (
      'VALID',
      'MANUALLY_EDITED',
      'ESTIMATED_USING_REFERENCE_DAY',
      'ESTIMATED_USING_LINEAR_INTERPOLATION',
      'QUESTIONABLE',
      'DERIVED',
      'PROJECTED_FORECAST',
      'MIXED',
      'RAW',
      'NORMALIZED_FOR_WEATHER',
      'OTHER',
      'VALIDATED',
      'VERIFIED',
      'REVENUE_QUALITY'
    );
  END IF;
END $$;

CREATE TABLE IF NOT EXISTS usage.interval_block (
  uuid UUID PRIMARY KEY,
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  up_link_href TEXT,
  updated TIMESTAMP,
  duration BIGINT,
  start BIGINT,
  meter_reading_uuid UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS usage.interval_reading (
  id BIGSERIAL PRIMARY KEY,
  cost BIGINT,
  duration BIGINT,
  start BIGINT,
  value BIGINT,
  consumption_tier SMALLINT,
  tou SMALLINT,
  cpp SMALLINT,
  block_uuid UUID NOT NULL REFERENCES usage.interval_block ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usage.reading_quality (
  id BIGSERIAL PRIMARY KEY,
  quality usage.quality_of_reading NOT NULL,
  reading_id BIGINT NOT NULL REFERENCES usage.interval_reading ON DELETE CASCADE
);
