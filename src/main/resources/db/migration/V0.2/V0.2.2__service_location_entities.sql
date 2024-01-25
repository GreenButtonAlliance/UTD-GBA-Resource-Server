CREATE TABLE IF NOT EXISTS customer.service_location (
                                                       uuid                UUID PRIMARY KEY NOT NULL,
                                                       description         TEXT,
                                                       published           TIMESTAMP,
                                                       self_link_href      TEXT,
                                                       up_link_href        TEXT,
                                                       updated             TIMESTAMP,
                                                       type                TEXT,
  main_address BIGSERIAL REFERENCES customer.street_address ON DELETE CASCADE,
  secondary_address BIGSERIAL REFERENCES customer.street_address ON DELETE CASCADE,
  phone1 BIGSERIAL REFERENCES customer.telephone_number ON DELETE CASCADE,
  phone2 BIGSERIAL REFERENCES customer.telephone_number ON DELETE CASCADE,
  electronic_address BIGSERIAL REFERENCES customer.electronic_address ON DELETE CASCADE,
                                                       geo_info_reference  TEXT,
                                                       direction           TEXT,
                                                       status              TEXT,
                                                       x_position          TEXT,
                                                       y_position          TEXT,
                                                       z_position          TEXT,
                                                       access_method       TEXT,
                                                       site_access_problem TEXT,
                                                       needs_inspection    boolean,
                                                       outage_block        TEXT
);
CREATE TABLE IF NOT EXISTS customer.usage_points (
                                                   usage_point_uuid UUID REFERENCES customer.service_location ON DELETE CASCADE,
                                                   usage_point      TEXT,
                                                   PRIMARY KEY (usage_point_uuid, usage_point)
);
