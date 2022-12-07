--Change tariffRiderRef to use an integer id
--so it can be referenced by multiple entities
ALTER TABLE usage.tariff_rider_ref DROP CONSTRAINT tariff_rider_ref_pkey;
ALTER TABLE usage.tariff_rider_ref DROP COLUMN usage_summary_uuid;
ALTER TABLE usage.tariff_rider_ref ADD COLUMN id BIGSERIAL PRIMARY KEY;

--update usage summaries to use new tariffRiderRef
ALTER TABLE usage.usage_summaries ADD COLUMN tariff_rider_ref_id BIGINT REFERENCES usage.tariff_rider_ref ON DELETE CASCADE;

