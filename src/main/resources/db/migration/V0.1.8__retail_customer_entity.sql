CREATE TABLE IF NOT EXISTS usage.retail_customer (
                                                      uuid UUID PRIMARY KEY,
                                                      description TEXT,
                                                      published TIMESTAMP,
                                                      self_link_href TEXT,
                                                      self_link_rel TEXT,
                                                      up_link_href TEXT,
                                                      up_link_rel TEXT,
                                                      updated TIMESTAMP,
  --tinyblob doesn't exist in postgres, bytea seems to be the closest equvilant
                                                      enabled BOOLEAN,
                                                      first_name TEXT,
                                                      last_name TEXT,
                                                      password TEXT,
                                                      role TEXT,
                                                      username TEXT

)
