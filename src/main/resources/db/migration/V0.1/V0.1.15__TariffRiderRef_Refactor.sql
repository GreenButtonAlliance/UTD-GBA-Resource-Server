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

--Change tariffRiderRef to use an integer id
--so it can be referenced by multiple entities
ALTER TABLE usage.tariff_rider_ref DROP CONSTRAINT tariff_rider_ref_pkey;
ALTER TABLE usage.tariff_rider_ref DROP COLUMN usage_summary_uuid;
ALTER TABLE usage.tariff_rider_ref ADD COLUMN id BIGSERIAL PRIMARY KEY;

--add table to connect tariffRiderRef and UsageSummary'
CREATE TABLE IF NOT EXISTS usage.usage_summaries_tariff_rider_ref (
  usage_summary_uuid UUID REFERENCES usage.usage_summaries ON DELETE CASCADE,
  tariff_rider_ref_id BIGINT REFERENCES usage.tariff_rider_ref ON DELETE CASCADE,
  PRIMARY KEY (usage_summary_uuid, tariff_rider_ref_id)
);

