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


CREATE TABLE IF NOT EXISTS usage.service_delivery_points (
                                                           uuid UUID PRIMARY KEY,
                                                           customerAgreement TEXT,
                                                           name TEXT,
                                                           tariffProfile TEXT
);

CREATE TABLE IF NOT EXISTS usage.usage_points (
                                                   uuid UUID PRIMARY KEY,
                                                   description TEXT,
                                                   published TIMESTAMP,
                                                   self_link_href TEXT,
                                                   self_link_rel TEXT,
                                                   up_link_href TEXT,
                                                   up_link_rel TEXT,
                                                   updated TIMESTAMP,
                                                   status SMALLINT,
                                                   uri TEXT,
                                                   localTimeParameters_id UUID REFERENCES  usage.time_configuration ON DELETE CASCADE,
                                                   retail_customer_id UUID REFERENCES  usage.retail_customer ON DELETE CASCADE,
                                                   serviceCategory_kind BIGINT,
                                                   serviceDeliveryPoint_id UUID REFERENCES usage.service_delivery_points ON DELETE CASCADE,
                                                   subscription_id UUID,
                                                   am_i_billing_ready TEXT,
                                                   check_billing BOOLEAN,
                                                   connection_state TEXT,
                                                   estimated_load TEXT,
                                                   grounded BOOLEAN,
                                                   is_sdp BOOLEAN,
                                                   is_virtual BOOLEAN,
                                                   minimal_usage_expected BOOLEAN,
                                                   nominal_service_voltage TEXT,
                                                   outage_region TEXT,
                                                   phase_code TEXT,
                                                   rated_current TEXT,
                                                   rated_power TEXT,
                                                   read_cycle TEXT,
                                                   read_route TEXT,
                                                   service_delivery_remark TEXT,
                                                   service_priority TEXT,
                                                   pnode_refs TEXT,
                                                   aggregate_node_refs TEXT
);
