CREATE TABLE IF NOT EXISTS usage.subscription (
                                                uuid UUID PRIMARY KEY,
                                                description TEXT,
                                                published TIMESTAMP,
                                                self_link_href TEXT,
                                                self_link_rel TEXT,
                                                up_link_href TEXT,
                                                up_link_rel TEXT,
                                                updated TIMESTAMP,
                                                hashedId TEXT,
                                                lastUpdate TIMESTAMP,
                                                applicationInformation_id BIGINT, 
                                                authorization_id BIGINT,
                                                retail_customer_id BIGINT,
                                                usagepoint_id INT
);
