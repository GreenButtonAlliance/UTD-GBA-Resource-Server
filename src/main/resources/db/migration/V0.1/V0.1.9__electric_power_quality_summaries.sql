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

CREATE TABLE IF NOT EXISTS usage.electric_power_quality_summaries (
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  self_link_rel TEXT,
  up_link_href TEXT,
  up_link_rel TEXT,
  updated TIMESTAMP,
  uuid UUID NOT NULL PRIMARY KEY,
  flicker_plt BIGINT,
  flicker_pst BIGINT,
  harmonic_voltage BIGINT,
  long_interruptions BIGINT,
  mains_voltage BIGINT,
  measurement_protocol SMALLINT,
  power_frequency BIGINT,
  rapid_voltage_changes BIGINT,
  short_interruptions BIGINT,
  duration BIGINT,
  start BIGINT,
  supply_voltage_dips BIGINT,
  supply_voltage_imbalance BIGINT,
  supply_voltage_variations BIGINT,
  temp_overvoltage BIGINT
--   TODO add reference to usage_points uuid EX: usage_point_uuid UUID NOT NULL REFERENCES usage.usage_points ON DELETE CASCADE
);
