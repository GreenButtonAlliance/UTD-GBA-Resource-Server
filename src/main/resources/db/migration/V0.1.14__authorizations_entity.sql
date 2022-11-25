DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'o_auth_error' AND ns.nspname = 'usage') THEN
      CREATE TYPE usage.o_auth_error AS ENUM (
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
                                                      error usage.o_auth_error,
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
