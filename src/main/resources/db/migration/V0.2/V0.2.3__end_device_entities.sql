/*
 * Copyright (c) 2024 Green Button Alliance, Inc.
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

CREATE TABLE IF NOT EXISTS customer.end_device (
  uuid UUID PRIMARY KEY NOT NULL,
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  up_link_href TEXT,
  updated TIMESTAMP,
  type TEXT,
  utc_number TEXT,
  serial_number TEXT,
  lot_number TEXT,
  purchase_price BIGINT,
  critical BOOLEAN,
  electronic_address_id BIGINT REFERENCES customer.electronic_address ON DELETE CASCADE,
  manufactured_date BIGINT,
  purchase_date BIGINT,
  received_date BIGINT,
  installation_date BIGINT,
  removal_date BIGINT,
  retired_date BIGINT,
  acceptance_type TEXT,
  acceptance_success BOOLEAN,
  acceptance_date_time BIGINT,
  initial_condition TEXT,
  initial_loss_of_life SMALLINT,
  status_id BIGINT REFERENCES customer.status ON DELETE CASCADE,
  is_virtual BOOLEAN,
  is_pan BOOLEAN,
  install_code TEXT,
  amr_system TEXT
);
