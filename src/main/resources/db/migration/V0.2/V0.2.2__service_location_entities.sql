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

CREATE TABLE IF NOT EXISTS customer.service_location (
   description TEXT,
   published TIMESTAMP,
   self_link_href TEXT,
   self_link_rel TEXT,
   up_link_href TEXT,
   up_link_rel TEXT,
   updated TIMESTAMP,
   uuid UUID PRIMARY KEY NOT NULL,
   type TEXT,
  main_address BIGSERIAL REFERENCES customer.street_address ON DELETE CASCADE,
  secondary_address BIGSERIAL REFERENCES customer.street_address ON DELETE CASCADE,
  phone1 BIGSERIAL REFERENCES customer.telephone_number ON DELETE CASCADE,
  phone2 BIGSERIAL REFERENCES customer.telephone_number ON DELETE CASCADE,
  electronic_address BIGSERIAL REFERENCES customer.electronic_address ON DELETE CASCADE,
   geo_info_reference TEXT,
   direction TEXT,
   status TEXT,
   x_position TEXT,
   y_position TEXT,
   z_position TEXT,
   access_method TEXT,
   site_access_problem TEXT,
   needs_inspection boolean,
   outage_block TEXT
);
CREATE TABLE IF NOT EXISTS customer.usage_points (
     usage_point_uuid UUID REFERENCES customer.service_location ON DELETE CASCADE,
     usage_point TEXT,
     PRIMARY KEY (usage_point_uuid, usage_point)
);
