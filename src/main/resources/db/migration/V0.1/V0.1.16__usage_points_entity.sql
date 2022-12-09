DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'service_kind' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.service_kind AS ENUM (
        'ELECTRICITY',
        'GAS',
        'WATER',
        'TIME',
        'HEAT',
        'REFUSE',
        'SEWERAGE',
        'RATES',
        'TVLICENSE',
        'INTERNET',
        'WEATHER'
        );
    END IF;
  END $$;

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'am_i_billing_ready_kind' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.am_i_billing_ready_kind AS ENUM (
        'AMICAPABLE',
        'AMIDISABLED',
        'BILLINGAPPROVED',
        'ENABLED',
        'NONAMI',
        'NONMETERED',
        'OPERABLE'
        );
    END IF;
  END $$;

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'usage_point_connected_kind' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.usage_point_connected_kind AS ENUM (
        'CONNECTED',
        'LOGICALLYDISCONNECTED',
        'PHYSICALLYDISCONNECTED'
        );
    END IF;
  END $$;

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'apnode_type' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.apnode_type AS ENUM (
        'AG',
        'CPZ',
        'DPZ',
        'LAP',
        'TH',
        'SYS',
        'CA',
        'DCA',
        'GA',
        'GH',
        'EHV',
        'ZN',
        'INT',
        'BUS'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'anode_type' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.anode_type AS ENUM (
        'SYS',
        'RUC',
        'LFZ',
        'REG',
        'AGR',
        'POD',
        'ALR',
        'LTAC',
        'ACA',
        'ASR',
        'ECA'
        );
    END IF;
  END $$;


CREATE TABLE IF NOT EXISTS usage.service_delivery_point (
  uuid UUID PRIMARY KEY,
  name TEXT,
  tariff_profile TEXT,
  customer_agreement TEXT
);

CREATE TABLE IF NOT EXISTS usage.usage_point (
  uuid UUID PRIMARY KEY,
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  self_link_rel TEXT,
  up_link_href TEXT,
  up_link_rel TEXT,
  updated TIMESTAMP,
  role_flags BYTEA,
  --service_category
  status SMALLINT,
  service_delivery_point_uuid UUID REFERENCES usage.service_delivery_point ON DELETE CASCADE,
  am_i_billing_ready usage.am_i_billing_ready_kind,
  check_billing BOOLEAN,
  connection_state usage.usage_point_connected_kind,
  estimated_load_power_of_ten_multiplier usage.unit_multiplier_kind,
  estimated_load_time_stamp BIGINT,
  estimated_load_uom usage.unit_symbol_kind,
  estimated_load_value BIGINT,
  estimated_load_reading_type_ref TEXT,
  grounded BOOLEAN,
  is_sdp BOOLEAN,
  is_virtual BOOLEAN,
  minimal_usage_expected BOOLEAN,
  nominal_service_voltage_power_of_ten_multiplier usage.unit_multiplier_kind,
  nominal_service_voltage_time_stamp BIGINT,
  nominal_service_voltage_uom usage.unit_symbol_kind,
  nominal_service_voltage_value BIGINT,
  nominal_service_voltage_reading_type_ref TEXT,
  outage_region TEXT,
  phase_code usage.phase_code_kind,
  rated_current_power_of_ten_multiplier usage.unit_multiplier_kind,
  rated_current_time_stamp BIGINT,
  rated_current_uom usage.unit_symbol_kind,
  rated_current_value BIGINT,
  rated_current_reading_type_ref TEXT,
  rated_power_power_of_ten_multiplier usage.unit_multiplier_kind,
  rated_power_time_stamp BIGINT,
  rated_power_uom usage.unit_symbol_kind,
  rated_power_value BIGINT,
  rated_power_reading_type_ref TEXT,
  read_cycle TEXT,
  read_route TEXT,
  service_delivery_remark TEXT,
  service_priority TEXT
);

CREATE TABLE IF NOT EXISTS usage.service_delivery_point_tariff_rider_ref (
                                                                    service_delivery_point_uuid UUID REFERENCES usage.service_delivery_point ON DELETE CASCADE,
                                                                    tariff_rider_ref_id BIGINT REFERENCES usage.tariff_rider_ref ON DELETE CASCADE,
                                                                    PRIMARY KEY (service_delivery_point_uuid, tariff_rider_ref_id)
);

CREATE TABLE IF NOT EXISTS usage.pnode_ref (
  id BIGSERIAL PRIMARY KEY,
  apnode_type usage.apnode_type,
  ref TEXT,
  start_effective_date BIGINT,
  end_effective_date BIGINT
);

CREATE TABLE IF NOT EXISTS usage.usage_point_pnode_ref (
                                                                           usage_point_uuid UUID REFERENCES usage.usage_point ON DELETE CASCADE,
                                                                           pnode_ref_id BIGINT REFERENCES usage.pnode_ref ON DELETE CASCADE,
                                                                           PRIMARY KEY (usage_point_uuid, pnode_ref_id)
);

CREATE TABLE IF NOT EXISTS usage.aggregate_node_ref (
                                             id BIGSERIAL PRIMARY KEY,
                                             anode_type usage.anode_type,
                                             ref TEXT,
                                             start_effective_date BIGINT,
                                             end_effective_date BIGINT,
                                             pnode_id BIGINT REFERENCES usage.pnode_ref
);

CREATE TABLE IF NOT EXISTS usage.usage_point_aggregate_node_ref (
                                                                           usage_point_uuid UUID REFERENCES usage.usage_point ON DELETE CASCADE,
                                                                           aggregate_node_ref_id BIGINT REFERENCES usage.aggregate_node_ref ON DELETE CASCADE,
                                                                           PRIMARY KEY (usage_point_uuid, aggregate_node_ref_id)
);
