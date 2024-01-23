DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'notification_method_kind' AND ns.nspname = 'customer') THEN
    CREATE TYPE customer.notification_method_kind AS ENUM (
      'CALL',
      'EMAIL',
      'LETTER',
      'OTHER',
      'IVR'
    );
  END IF;
END $$;

CREATE TABLE IF NOT EXISTS customer.status (
  id BIGSERIAL PRIMARY KEY,
  status_value TEXT,
  date_time BIGINT,
  remark TEXT,
  reason TEXT
);

CREATE TABLE IF NOT EXISTS customer.electronic_address (
  id BIGSERIAL PRIMARY KEY,
  lan TEXT,
  mac TEXT,
  email1 TEXT,
  email2 TEXT,
  web TEXT,
  radio TEXT,
  user_id TEXT,
  password TEXT
);

CREATE TABLE IF NOT EXISTS customer.street_address (
  id BIGSERIAL PRIMARY KEY,
  street_number TEXT,
  street_name TEXT,
  street_suffix TEXT,
  street_prefix TEXT,
  street_type TEXT,
  street_code TEXT,
  street_building_name TEXT,
  street_suite_number TEXT,
  street_address_general TEXT,
  street_address_general2 TEXT,
  street_address_general3 TEXT,
  street_within_town_limits BOOLEAN,
  town_code TEXT,
  town_section TEXT,
  town_name TEXT,
  town_county TEXT,
  town_state_or_province TEXT,
  town_country TEXT,
  status_id BIGINT REFERENCES customer.status ON DELETE CASCADE,
  postal_code TEXT,
  po_box TEXT
);

CREATE TABLE IF NOT EXISTS customer.telephone_number (
  id BIGSERIAL PRIMARY KEY,
  country_code TEXT,
  area_code TEXT,
  city_code TEXT,
  local_number TEXT,
  ext TEXT,
  dial_out TEXT,
  international_prefix TEXT,
  itu_phone TEXT
);

CREATE TABLE IF NOT EXISTS customer.organisation (
  id BIGSERIAL PRIMARY KEY,
  street_address_id BIGINT REFERENCES customer.street_address ON DELETE CASCADE,
  postal_address_id BIGINT REFERENCES customer.street_address ON DELETE CASCADE,
  phone1_id BIGINT REFERENCES customer.telephone_number ON DELETE CASCADE,
  phone2_id BIGINT REFERENCES customer.street_address ON DELETE CASCADE,
  electronic_address_id BIGINT REFERENCES customer.electronic_address ON DELETE CASCADE,
  organisation_name TEXT
);

CREATE TABLE IF NOT EXISTS customer.customer_account (
  uuid UUID PRIMARY KEY,
  description    TEXT,
  published      TIMESTAMP,
  self_link_href TEXT,
  up_link_href   TEXT,
  updated        TIMESTAMP,
  type TEXT,
  author_name TEXT,
  created_date_time BIGINT,
  last_modified_date_time BIGINT,
  revision_number TEXT,
  electronic_address_id BIGINT REFERENCES customer.electronic_address ON DELETE CASCADE,
  subject TEXT,
  title TEXT,
  doc_status_id BIGINT REFERENCES customer.status ON DELETE CASCADE,
  status_id BIGINT REFERENCES customer.status ON DELETE CASCADE,
  comment TEXT,
  billing_cycle TEXT,
  budget_bill TEXT,
  last_bill_amount BIGINT,
  contact_info_id BIGINT REFERENCES customer.organisation ON DELETE CASCADE,
  account_id TEXT
);

CREATE TABLE IF NOT EXISTS customer.account_notification (
  id BIGSERIAL PRIMARY KEY,
  method_kind customer.notification_method_kind,
  notification_time BIGINT,
  customer_notification_kind TEXT,
  customer_account_id UUID NOT NULL REFERENCES customer.customer_account
);
