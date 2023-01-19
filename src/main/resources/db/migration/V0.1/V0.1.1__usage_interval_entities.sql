/*
 * Copyright (c) 2022-2023 Green Button Alliance, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  self_link_rel TEXT,
  up_link_href TEXT,
  up_link_rel TEXT,
  updated TIMESTAMP
);

CREATE TABLE IF NOT EXISTS usage.interval_reading (
  id BIGSERIAL PRIMARY KEY,
  cost BIGINT,
  start BIGINT,
  duration INTEGER,
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
