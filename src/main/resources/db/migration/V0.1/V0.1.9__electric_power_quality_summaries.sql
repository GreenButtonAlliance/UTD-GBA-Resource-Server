CREATE TABLE IF NOT EXISTS usage.electric_power_quality_summaries (
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  up_link_href TEXT,
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
