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
