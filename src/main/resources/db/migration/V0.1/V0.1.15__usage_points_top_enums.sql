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
