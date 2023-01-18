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

CREATE TABLE IF NOT EXISTS usage.time_configuration (
                                                      uuid UUID PRIMARY KEY,
                                                      description TEXT,
                                                      published TIMESTAMP,
                                                      self_link_href TEXT,
                                                      self_link_rel TEXT,
                                                      up_link_href TEXT,
                                                      up_link_rel TEXT,
                                                      updated TIMESTAMP,
  --tinyblob doesn't exist in postgres, bytea seems to be the closest equivalent
                                                      dst_end_rule BYTEA,
                                                      dst_offset BIGINT,
                                                      dst_start_rule BYTEA,
                                                      tz_offset BIGINT
)
