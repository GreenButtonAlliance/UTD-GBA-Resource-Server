CREATE TABLE IF NOT EXISTS usage.subscription (
                                                uuid UUID PRIMARY KEY,
                                                description TEXT,
                                                published TIMESTAMP,
                                                self_link_href TEXT,
                                                up_link_href TEXT,
                                                updated TIMESTAMP,
                                                hashed_id TEXT,
                                                last_update TIMESTAMP,
                                                application_information_id UUID REFERENCES usage.application_information,
                                                authorization_id UUID REFERENCES usage.authorization,
                                                retail_customer_id UUID REFERENCES usage.retail_customer,
                                                usage_point_id UUID --TODO
);

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_constraint WHERE conname = 'fk_subscription') THEN
      ALTER TABLE usage.authorization ADD CONSTRAINT fk_subscription FOREIGN KEY (subscription_id) REFERENCES
        usage.subscription;
    END IF;
  END $$;
