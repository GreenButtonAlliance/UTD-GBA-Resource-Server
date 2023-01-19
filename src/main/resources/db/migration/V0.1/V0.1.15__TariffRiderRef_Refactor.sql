--add table to connect tariffRiderRef and UsageSummary'
CREATE TABLE IF NOT EXISTS usage.usage_summaries_tariff_rider_ref (
  usage_summary_uuid UUID REFERENCES usage.usage_summaries ON DELETE CASCADE,
  tariff_rider_ref_id BIGINT REFERENCES usage.tariff_rider_ref ON DELETE CASCADE,
  PRIMARY KEY (usage_summary_uuid, tariff_rider_ref_id)
);

