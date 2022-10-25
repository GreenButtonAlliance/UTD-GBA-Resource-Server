CREATE TABLE IF NOT EXISTS usage.meter_reading (
  uuid UUID PRIMARY KEY,
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  self_link_rel TEXT,
  up_link_href TEXT,
  up_link_rel TEXT,
  updated TIMESTAMP,
  reading_type_uuid UUID REFERENCES usage.reading_type ON DELETE CASCADE
);

-- Only need this sample data to add not-null constraint to interval_block -> meter_reading reference while preserving existing interval_block data
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM usage.meter_reading mr WHERE mr.uuid = 'F77FBF34-A09E-4EBC-9606-FF1A59A17CAE') THEN
    INSERT INTO usage.meter_reading
    VALUES ('F77FBF34-A09E-4EBC-9606-FF1A59A17CAE', 'Fifteen Minute Electricity Consumption', '2012-10-24 04:00:00', 'https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01', 'self', 'https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01', 'up', '2012-10-24 04:00:00', NULL);
  END IF;
END $$;

ALTER TABLE usage.interval_block ADD COLUMN IF NOT EXISTS meter_reading_uuid UUID NOT NULL DEFAULT 'F77FBF34-A09E-4EBC-9606-FF1A59A17CAE' REFERENCES usage.meter_reading ON DELETE CASCADE;
ALTER TABLE usage.interval_block ALTER COLUMN meter_reading_uuid DROP DEFAULT;
