CREATE TABLE IF NOT EXISTS usage.usage_points (
                                                   uuid UUID PRIMARY KEY,
                                                   description TEXT,
                                                   published TIMESTAMP,
                                                   self_link_href TEXT,
                                                   self_link_rel TEXT,
                                                   up_link_href TEXT,
                                                   up_link_rel TEXT,
                                                   updated TIMESTAMP,
                                                   status SMALLINT,
                                                   uri TEXT,
                                                   localTimeParameters_id UUID REFERENCES  usage.time_configuration ON DELETE CASCADE,
                                                   retail_customer_id UUID REFERENCES  usage.retail_customer ON DELETE CASCADE,
                                                   serviceCategory_kind BIGINT,
                                                   serviceDeliveryPoint_id UUID REFERENCES usage.service_delivery_points ON DELETE CASCADE,
                                                   subscription_id UUID
)
