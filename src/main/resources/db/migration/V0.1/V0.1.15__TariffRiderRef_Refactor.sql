--Change tariffRiderRef to use an integer id
--so it can be referenced by multiple entities
DO
$$
  BEGIN
    IF EXISTS (SELECT 1
               FROM information_schema.columns
               WHERE table_schema = 'usage'
                 AND table_name = 'tariff_rider_ref'
                 AND column_name = 'tariff_rider_ref_pkey')
    THEN
      ALTER TABLE usage.tariff_rider_ref
        DROP CONSTRAINT tariff_rider_ref_pkey;
    END IF;
  END
$$;

DO
$$
  BEGIN
    IF EXISTS (SELECT 1
               FROM information_schema.columns
               WHERE table_schema = 'usage'
                 AND table_name = 'tariff_rider_ref'
                 AND column_name = 'usage_summary_uuid')
    THEN
      ALTER TABLE usage.tariff_rider_ref
        DROP COLUMN usage_summary_uuid;
    END IF;
  END
$$;

DO
$$
  BEGIN
    IF NOT EXISTS (SELECT 1
                   FROM information_schema.columns
                   WHERE table_schema = 'usage'
                     AND table_name = 'tariff_rider_ref'
                     AND column_name = 'id')
    THEN
      ALTER TABLE usage.tariff_rider_ref
        ADD COLUMN id BIGSERIAL PRIMARY KEY;
    END IF;
  END
$$;

--add table to connect tariffRiderRef and UsageSummary'
CREATE TABLE IF NOT EXISTS usage.usage_summaries_tariff_rider_ref (
  usage_summary_uuid UUID REFERENCES usage.usage_summaries ON DELETE CASCADE,
  tariff_rider_ref_id BIGINT REFERENCES usage.tariff_rider_ref ON DELETE CASCADE,
  PRIMARY KEY (usage_summary_uuid, tariff_rider_ref_id)
);

