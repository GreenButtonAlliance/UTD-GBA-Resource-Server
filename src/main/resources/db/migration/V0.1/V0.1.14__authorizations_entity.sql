/*
 * Copyright (c) 2022-2023 Green Button Alliance, Inc.
 *
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
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'o_auth_error' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.oauth_error AS ENUM (
        'INVALID_REQUEST',
        'INVALID_CLIENT',
        'INVALID_GRANT',
        'UNAUTHORIZED_CLIENT',
        'UNSUPPORTED_GRANT_TYPE',
        'INVALID_SCOPE',
        'INVALID_REDIRECT_URI',
        'INVALID_CLIENT_METADATA',
        'INVALID_CLIENT_ID',
        'ACCESS_DENIED',
        'UNSUPPORTED_RESPONSE_TYPE',
        'SERVER_ERROR',
        'TEMPORARILY_UNAVAILABLE'
        );
    END IF;
  END $$;

CREATE TABLE IF NOT EXISTS usage.authorization (
                                                      uuid UUID PRIMARY KEY,
                                                      description TEXT,
                                                      published TIMESTAMP,
                                                      self_link_href TEXT,
                                                      self_link_rel TEXT,
                                                      up_link_href TEXT,
                                                      up_link_rel TEXT,
                                                      updated TIMESTAMP,
                                                      access_token TEXT,
                                                      authorization_uri TEXT,
                                                      ap_duration BIGINT,
                                                      ap_start BIGINT,
                                                      code TEXT,
                                                      error usage.oauth_error,
                                                      error_description TEXT,
                                                      error_uri TEXT,
                                                      expires_in BIGINT,
                                                      grant_type INT,
                                                      pp_duration BIGINT,
                                                      pp_start BIGINT,
                                                      refresh_token TEXT,
                                                      resource_uri TEXT,
                                                      response_type INT,
                                                      scope TEXT,
                                                      state TEXT,
                                                      third_party TEXT,
                                                      token_type INT,
                                                      application_information_id UUID,
                                                      retail_customer_id UUID,
                                                      subscription_id UUID
)
