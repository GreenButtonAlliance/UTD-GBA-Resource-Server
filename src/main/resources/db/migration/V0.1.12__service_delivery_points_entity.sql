DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'time_period_of_interest' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.time_period_of_interest AS ENUM (
        'NONE',
        'BILLING_PERIOD',
        'DAILY',
        'MONTHLY',
        'SEASONAL',
        'WEEKLY',
        'SPECIFIED_PERIOD'
        );
    END IF;
  END $$;

CREATE TABLE IF NOT EXISTS usage.service_delivery_points (
                                                      uuid UUID PRIMARY KEY,
                                                      customerAgreement TEXT,
                                                      name TEXT,
                                                      tariffProfile TEXT
)
