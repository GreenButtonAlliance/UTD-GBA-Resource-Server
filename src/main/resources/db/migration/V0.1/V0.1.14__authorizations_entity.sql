DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'oauth_error' AND ns.nspname = 'usage') THEN
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
                                                      authorized_period_start BIGINT,
                                                      authorized_period_duration BIGINT,
                                                      published_period_start BIGINT,
                                                      published_period_duration BIGINT,
                                                      status TEXT,
                                                      expires_at BIGINT,
                                                      grant_type TEXT,
                                                      scope TEXT,
                                                      token_type TEXT,
                                                      error TEXT,
                                                      error_description TEXT,
                                                      error_uri TEXT,
                                                      resource_uri TEXT,
                                                      authorization_uri TEXT,
                                                      customer_resource_uri TEXT,
                                                      application_information_id UUID,
                                                      retail_customer_id UUID,
                                                      subscription_id UUID
)
