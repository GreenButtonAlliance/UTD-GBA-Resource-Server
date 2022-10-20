CREATE TABLE IF NOT EXISTS usage.time_configuration (
                                                      uuid UUID PRIMARY KEY,
                                                      description TEXT,
                                                      published TIMESTAMP,
                                                      self_link_href TEXT,
                                                      self_link_rel TEXT,
                                                      up_link_href TEXT,
                                                      up_link_rel TEXT,
                                                      updated TIMESTAMP,
  --tinyblob doesn't exist in postgres, bytea seems to be the closest equvilant
                                                      dst_end_rule BYTEA,
                                                      dst_offset BIGINT,
                                                      dst_start_rule BYTEA,
                                                      tz_offset BIGINT
)
