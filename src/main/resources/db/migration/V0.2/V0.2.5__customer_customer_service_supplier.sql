DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'customer_kind' AND ns.nspname = 'customer') THEN
    CREATE TYPE customer.customer_kind AS ENUM (
      'RESIDENTIAL',
      'RESIDENTIAL_AND_COMMERCIAL',
      'RESIDENTIAL_AND_STREETLIGHT',
      'RESIDENTIAL_STREETLIGHT_OTHERS',
      'RESIDENTIAL_FARM_SERVICE',
      'COMMERCIAL_INDUSTRIAL',
      'PUMPING_LOAD',
      'WIND_MACHINE',
      'ENERGY_SERVICE_SUPPLIER',
      'ENERGY_SERVICE_SCHEDULER',
      'ENTERPRISE',
      'REGIONAL_OPERATOR',
      'SUBSIDIARY',
      'INTERNAL_USE',
      'OTHER'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'supplier_kind' AND ns.nspname = 'customer') THEN
    CREATE TYPE customer.supplier_kind AS ENUM (
      'UTILITY',
      'RETAILER',
      'OTHER',
      'LSA',
      'MDMA',
      'MSP'
    );
  END IF;
END $$;

-- change schema of a few types to public since we'll be using them on the customer side as well
DO $$
  BEGIN
    IF EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'enrollment_status' AND ns.nspname = 'usage') THEN
      IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'enrollment_status' AND ns.nspname = 'public') THEN
        ALTER TYPE usage.enrollment_status SET SCHEMA public;
      END IF;
    END IF;
  END $$;

DO $$
  BEGIN
    IF EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'unit_multiplier_kind' AND ns.nspname = 'usage') THEN
      IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'unit_multiplier_kind' AND ns.nspname = 'public') THEN
        ALTER TYPE usage.unit_multiplier_kind SET SCHEMA public;
      END IF;
    END IF;
  END $$;

DO $$
  BEGIN
    IF EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'unit_symbol_kind' AND ns.nspname = 'usage') THEN
      IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'unit_symbol_kind' AND ns.nspname = 'public') THEN
        ALTER TYPE usage.unit_symbol_kind SET SCHEMA public;
      END IF;
    END IF;
  END $$;

DO $$
  BEGIN
    IF EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'currency' AND ns.nspname = 'usage') THEN
      IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'currency' AND ns.nspname = 'public') THEN
        ALTER TYPE usage.currency SET SCHEMA public;
      END IF;
    END IF;
  END $$;

CREATE TABLE IF NOT EXISTS customer.priority (
  id BIGSERIAL PRIMARY KEY,
  rank BIGINT NOT NULL,
  type TEXT,
  justification TEXT
);

CREATE TABLE IF NOT EXISTS customer.customer (
  uuid UUID PRIMARY KEY,
  organisation_id BIGINT REFERENCES customer.organisation ON DELETE SET NULL,
  kind customer.customer_kind,
  special_need TEXT,
  vip BOOLEAN,
  puc_number TEXT,
  status_id BIGINT REFERENCES customer.status ON DELETE SET NULL,
  priority_id BIGINT REFERENCES customer.priority ON DELETE SET NULL,
  locale TEXT,
  customer_name TEXT
);

CREATE TABLE IF NOT EXISTS customer.customer_agreement (
  uuid UUID PRIMARY KEY,
  type TEXT,
  author_name TEXT,
  created_date_time BIGINT,
  last_modified_date_time BIGINT,
  revision_number TEXT,
  electronic_address_id BIGINT REFERENCES customer.electronic_address ON DELETE SET NULL,
  subject TEXT,
  title TEXT,
  doc_status_id BIGINT REFERENCES customer.status ON DELETE SET NULL,
  status_id BIGINT REFERENCES customer.status ON DELETE SET NULL,
  comment TEXT,
  sign_date BIGINT,
  start BIGINT,
  duration BIGINT,
  load_mgmt TEXT,
  is_pre_pay BOOLEAN,
  shut_off_date_time BIGINT,
  currency public.currency,
  agreement_id TEXT
);

CREATE TABLE IF NOT EXISTS customer.demand_response_program (
  id BIGSERIAL PRIMARY KEY,
  program_name TEXT,
  enrollment_status public.enrollment_status,
  program_description TEXT,
  capacity_reservation_level_potm public.unit_multiplier_kind,
  capacity_reservation_level_time_stamp BIGINT,
  capacity_reservation_level_uom public.unit_symbol_kind,
  capacity_reservation_level_value BIGINT,
  capacity_reservation_level_reading_type_ref TEXT,
  dr_program_nomination_potm public.unit_multiplier_kind,
  dr_program_nomination_level_time_stamp BIGINT,
  dr_program_nomination_level_uom public.unit_symbol_kind,
  dr_program_nomination_level_value BIGINT,
  dr_program_nomination_level_reading_type_ref TEXT,
  customer_agreement_id UUID NOT NULL REFERENCES customer.customer_agreement ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS customer.program_date (
  id BIGSERIAL PRIMARY KEY,
  program_date BIGINT,
  program_date_description TEXT,
  demand_response_program_id BIGINT NOT NULL REFERENCES customer.demand_response_program ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS customer.customer_agreement_pricing_structure (
  customer_agreement_uuid UUID REFERENCES customer.customer_agreement ON DELETE CASCADE,
  pricing_structure TEXT,
  PRIMARY KEY (customer_agreement_uuid, pricing_structure)
);

CREATE TABLE IF NOT EXISTS customer.customer_agreement_future_status (
  customer_agreement_uuid UUID REFERENCES customer.customer_agreement ON DELETE CASCADE,
  status_id BIGINT REFERENCES customer.status ON DELETE CASCADE,
  PRIMARY KEY (customer_agreement_uuid, status_id)
);

CREATE TABLE IF NOT EXISTS customer.service_supplier (
  uuid UUID PRIMARY KEY,
  organisation_id BIGINT REFERENCES customer.organisation ON DELETE SET NULL,
  kind customer.supplier_kind,
  issuer_identification_number TEXT,
  effective_date BIGINT
);
