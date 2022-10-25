DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'token_end_point_method' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.token_end_point_method AS ENUM (
        'BASIC'
        );
    END IF;
END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'third_party_application_use' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.third_party_application_use AS ENUM (
        'ENERGY_MANAGEMENT',
        'COMPARISONS',
        'GOVERNMENT',
        'ACADEMIC',
        'LAW_ENFORCEMENT'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'third_party_application_type' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.third_party_application_type AS ENUM (
        'WEB',
        'DESKTOP',
        'MOBILE',
        'DEVICE'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'third_party_application_status' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.third_party_application_status AS ENUM (
        'DEVELOPMENT',
        'REVIEW_TEST',
        'LIVE',
        'REMOVE'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'response_types' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.response_types AS ENUM (
        'CODE'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'grant_types' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.grant_types AS ENUM (
        'AUTHORIZATION_CODE',
        'CLIENT_CREDENTIALS',
        'REFRESH_TOKEN'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'data_custodian_application_status' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.data_custodian_application_status AS ENUM (
        'REVIEW',
        'PRODUCTION',
        'ON_HOLD',
        'REVOKED'
        );
    END IF;
  END $$;

CREATE TABLE IF NOT EXISTS usage.application_information
(
  uuid                                            UUID PRIMARY KEY,
  description                                     TEXT,
  published                                       TIMESTAMP,
  self_link_href                                  TEXT,
  self_link_rel                                   TEXT,
  up_link_href                                    TEXT,
  up_link_rel                                     TEXT,
  updated                                         TIMESTAMP,
  kind                                            TEXT,
  authorization_server_authorization_endpoint     TEXT,
  authorization_server_registration_endpoint      TEXT,
  authorization_server_token_endpoint             TEXT,
  authorization_server_uri                        TEXT,
  client_id                                       TEXT NOT NULL,
  client_id_issued_at                             BIGINT,
  client_name                                     TEXT,
  client_secret                                   TEXT,
  client_secret_expires_at                        BIGINT,
  client_uri                                      TEXT,
  contacts                                        bytea,
  data_custodian_application_status               usage.data_custodian_application_status,
  data_custodian_bulk_request_uri                 TEXT,
  data_custodian_default_batch_resource           TEXT,
  data_custodian_default_subscription_resource    TEXT,
  data_custodian_id                               TEXT,
  data_custodian_resource_endpoint                TEXT,
  data_custodian_third_party_selection_screen_uri TEXT,
  logo_uri                                        TEXT,
  policy_uri                                      TEXT,
  third_party_application_description             TEXT,
  third_party_application_status                  usage.third_party_application_status,
  third_party_application_type                    usage.third_party_application_type,
  third_party_application_use                     usage.third_party_application_use,
  third_party_phone                               TEXT,
  third_party_notify_uri                          TEXT,
  third_party_user_portal_screen                  TEXT,
  redirect_uri                                    TEXT,
  tos_uri                                         TEXT,
  software_id                                     TEXT,
  software_version                                TEXT,
  token_endpoint_auth_method                      usage.token_end_point_method,
  response_types                                  usage.response_types,
  registration_client_uri                         TEXT,
  registration_access_token                       TEXT
);
CREATE TABLE IF NOT EXISTS usage.application_information_scope
(
  uuid       UUID PRIMARY KEY,
  scope      TEXT,
  application_information_uuid UUID NOT NULL REFERENCES usage.application_information
);
CREATE TABLE IF NOT EXISTS usage.application_information_grant_type
(
  uuid                         UUID PRIMARY KEY,
  grant_types                  usage.grant_types,
  application_information_uuid UUID NOT NULL REFERENCES usage.application_information
);
