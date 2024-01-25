DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'item_kind' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.item_kind AS ENUM (
        'ENERGY_GENERATION_FEE',
        'ENERGY_DELIVERY_FEE',
        'ENERGY_USAGE_FEE',
        'ADMINISTRATIVE_FEE',
        'TAX',
        'ENERGY_GENERATION_CREDIT',
        'ENERGY_DELIVERY_CREDIT',
        'ADMINISTRATIVE_CREDIT',
        'PAYMENT',
        'INFORMATION'
        );
    END IF;
  END $$;

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'enrollment_status' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.enrollment_status AS ENUM (
        'UNENROLLED',
        'ENROLLED',
        'ENROLLED_PENDING'
        );
    END IF;
  END $$;

-- the abbreviate potm will be used for power_of_ten_multiplier
CREATE TABLE IF NOT EXISTS usage.usage_summaries (
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  up_link_href TEXT,
  updated TIMESTAMP,
  uuid UUID PRIMARY KEY NOT NULL,
  billing_period_start BIGINT,
  billing_period_duration BIGINT,
  bill_last_period BIGINT,
  bill_to_date BIGINT,
  cost_additional_last_period BIGINT,
  currency TEXT,
  overall_consumption_last_period_potm TEXT,
  overall_consumption_last_period_time_stamp BIGINT,
  overall_consumption_last_period_uom TEXT,
  overall_consumption_last_period_value BIGINT,
  overall_consumption_reading_type_ref TEXT,
  current_billing_period_overall_consumption_potm TEXT,
  current_billing_period_overall_consumption_time_stamp BIGINT,
  current_billing_period_overall_consumption_uom TEXT,
  current_billing_period_overall_consumption_value BIGINT,
  current_billing_period_overall_consumption_reading_type_ref TEXT,
  current_day_last_year_net_consumption_potm TEXT,
  current_day_last_year_net_consumption_time_stamp BIGINT,
  current_day_last_year_net_consumption_uom TEXT,
  current_day_last_year_net_consumption_value BIGINT,
  current_day_last_year_net_consumption_reading_type_ref TEXT,
  current_day_net_consumption_potm TEXT,
  current_day_net_consumption_time_stamp BIGINT,
  current_day_net_consumption_uom TEXT,
  current_day_net_consumption_value BIGINT,
  current_day_net_consumption_reading_type_ref TEXT,
  current_day_overall_consumption_potm TEXT,
  current_day_overall_consumption_time_stamp BIGINT,
  current_day_overall_consumption_uom TEXT,
  current_day_overall_consumption_value BIGINT,
  current_day_overall_consumption_reading_type_ref TEXT,
  peak_demand_potm TEXT,
  peak_demand_time_stamp BIGINT,
  peak_demand_uom TEXT,
  peak_demand_value BIGINT,
  peak_demand_reading_type_ref TEXT,
  previous_day_last_year_overall_consumption_potm TEXT,
  previous_day_last_year_overall_consumption_time_stamp BIGINT,
  previous_day_last_year_overall_consumption_uom TEXT,
  previous_day_last_year_overall_consumption_value BIGINT,
  previous_day_last_year_overall_consumption_reading_type_ref TEXT,
  previous_day_net_consumption_potm TEXT,
  previous_day_net_consumption_time_stamp BIGINT,
  previous_day_net_consumption_uom TEXT,
  previous_day_net_consumption_value BIGINT,
  previous_day_net_consumption_reading_type_ref TEXT,
  previous_day_overall_consumption_potm TEXT,
  previous_day_overall_consumption_time_stamp BIGINT,
  previous_day_overall_consumption_uom TEXT,
  previous_day_overall_consumption_value BIGINT,
  previous_day_overall_consumption_reading_type_ref TEXT,
  quality_of_reading TEXT,
  ratchet_demand_potm TEXT,
  ratchet_demand_time_stamp BIGINT,
  ratchet_demand_uom TEXT,
  ratchet_demand_value BIGINT,
  ratchet_demand_reading_type_ref TEXT,
  ratchet_demand_period_start BIGINT,
  ratchet_demand_period_duration BIGINT,
  status_time_stamp BIGINT NOT NULL,
  commodity TEXT,
  tariff_profile TEXT,
  read_cycle TEXT,
  agency_name TEXT
--   TODO add reference to usage_points uuid EX: usage_point_uuid UUID NOT NULL REFERENCES usage.usage_points ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS usage.line_item (
  id BIGINT PRIMARY KEY NOT NULL,
  amount BIGINT,
  rounding BIGINT,
  date_time BIGINT,
  note TEXT,
  power_of_ten_multiplier usage.unit_multiplier_kind,
  time_stamp BIGINT,
  uom usage.unit_symbol_kind,
  value BIGINT,
  reading_type_ref TEXT,
  item_kind usage.item_kind,
  unit_cost BIGINT,
  start BIGINT,
  duration BIGINT,
  usage_summary_uuid UUID NOT NULL REFERENCES usage.usage_summaries ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usage.line_item_sequence (
  line_item_seq BIGINT
);

CREATE TABLE IF NOT EXISTS usage.tariff_rider_ref (
  usage_summary_uuid UUID REFERENCES usage.usage_summaries ON DELETE CASCADE,
  rider_type TEXT,
  enrollment_status usage.enrollment_status,
  effective_date BIGINT,
  PRIMARY KEY (usage_summary_uuid, rider_type, enrollment_status, effective_date)
);
