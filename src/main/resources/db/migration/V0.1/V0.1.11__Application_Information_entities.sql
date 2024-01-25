/*
 * Copyright (c) 2024 Green Button Alliance, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (c) 2024 Green Button Alliance, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'token_endpoint_method' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.token_endpoint_method AS ENUM (
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
        'PRODUCTION',
        'RETIRED'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'response_type' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.response_type AS ENUM (
        'CODE'
        );
    END IF;
  END $$;
DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'grant_type' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.grant_type AS ENUM (
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
  up_link_href                                    TEXT,
  updated                                         TIMESTAMP,
  authorization_server_authorization_endpoint     TEXT NOT NULL,
  authorization_server_registration_endpoint      TEXT,
  authorization_server_token_endpoint             TEXT NOT NULL,
  authorization_server_uri                        TEXT,
  client_id                                       TEXT NOT NULL,
  client_id_issued_at                             BIGINT NOT NULL,
  client_name                                     TEXT NOT NULL,
  client_secret                                   TEXT NOT NULL,
  client_secret_expires_at                        BIGINT NOT NULL,
  client_uri                                      TEXT,
  data_custodian_application_status               usage.data_custodian_application_status NOT NULL,
  data_custodian_bulk_request_uri                 TEXT NOT NULL,
  data_custodian_id                               TEXT NOT NULL,
  data_custodian_resource_endpoint                TEXT NOT NULL,
  third_party_scope_selection_screen_uri          TEXT,
  third_party_user_portal_screen_uri              TEXT,
  logo_uri                                        TEXT,
  policy_uri                                      TEXT,
  third_party_application_description             TEXT,
  third_party_application_status                  usage.third_party_application_status,
  third_party_application_type                    usage.third_party_application_type,
  third_party_application_use                     usage.third_party_application_use,
  third_party_phone                               TEXT,
  third_party_notify_uri                          TEXT NOT NULL,
  tos_uri                                         TEXT,
  software_id                                     TEXT NOT NULL,
  software_version                                TEXT NOT NULL,
  token_endpoint_auth_method                      usage.token_endpoint_method NOT NULL,
  response_type                                   usage.response_type,
  registration_client_uri                         TEXT NOT NULL,
  registration_access_token                       TEXT NOT NULL,
  data_custodian_scope_selection_screen_uri       TEXT
);

CREATE TABLE IF NOT EXISTS usage.application_information_redirect_uri (
  application_information_uuid UUID REFERENCES usage.application_information ON DELETE CASCADE,
  redirect_uri TEXT,
  PRIMARY KEY (application_information_uuid, redirect_uri)
);

CREATE TABLE IF NOT EXISTS usage.application_information_contact (
  application_information_uuid UUID REFERENCES usage.application_information ON DELETE CASCADE,
  contact TEXT,
  PRIMARY KEY (application_information_uuid, contact)
);

CREATE TABLE IF NOT EXISTS usage.application_information_scope (
  application_information_uuid UUID REFERENCES usage.application_information ON DELETE CASCADE,
  scope TEXT,
  PRIMARY KEY (application_information_uuid, scope)
);

CREATE TABLE IF NOT EXISTS usage.application_information_grant_type (
  application_information_uuid UUID REFERENCES usage.application_information ON DELETE CASCADE,
  grant_type usage.grant_type,
  PRIMARY KEY (application_information_uuid, grant_type)
);
