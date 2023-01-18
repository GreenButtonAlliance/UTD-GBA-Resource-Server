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

-- change some ON DELETE CASCADE clauses to ON DELETE SET NULL for relationships whose referenced objects might be needed elsewhere even when a related object is deleted
ALTER TABLE usage.meter_reading DROP CONSTRAINT meter_reading_reading_type_uuid_fkey,
  ADD CONSTRAINT meter_reading_reading_type_uuid_fkey FOREIGN KEY (reading_type_uuid) REFERENCES usage.reading_type ON DELETE SET NULL;

ALTER TABLE customer.street_address DROP CONSTRAINT street_address_status_id_fkey,
  ADD CONSTRAINT street_address_status_id_fkey FOREIGN KEY (status_id) REFERENCES customer.status ON DELETE SET NULL;

ALTER TABLE customer.organisation DROP CONSTRAINT organisation_street_address_id_fkey,
  ADD CONSTRAINT organisation_street_address_id_fkey FOREIGN KEY (street_address_id) REFERENCES customer.street_address ON DELETE SET NULL;
ALTER TABLE customer.organisation DROP CONSTRAINT organisation_postal_address_id_fkey,
	ADD CONSTRAINT organisation_postal_address_id_fkey FOREIGN KEY (postal_address_id) REFERENCES customer.street_address ON DELETE SET NULL;
ALTER TABLE customer.organisation DROP CONSTRAINT organisation_phone1_id_fkey,
  ADD CONSTRAINT organisation_phone1_id_fkey FOREIGN KEY (phone1_id) REFERENCES customer.telephone_number ON DELETE SET NULL;
ALTER TABLE customer.organisation DROP CONSTRAINT organisation_phone2_id_fkey,
  ADD CONSTRAINT organisation_phone2_id_fkey FOREIGN KEY (phone2_id) REFERENCES customer.telephone_number ON DELETE SET NULL;
ALTER TABLE customer.organisation DROP CONSTRAINT organisation_electronic_address_id_fkey,
  ADD CONSTRAINT organisation_electronic_address_id_fkey FOREIGN KEY (electronic_address_id) REFERENCES customer.electronic_address ON DELETE SET NULL;

ALTER TABLE customer.customer_account DROP CONSTRAINT customer_account_electronic_address_id_fkey,
  ADD CONSTRAINT customer_account_electronic_address_id_fkey FOREIGN KEY (electronic_address_id) REFERENCES customer.electronic_address ON DELETE SET NULL;
ALTER TABLE customer.customer_account DROP CONSTRAINT customer_account_doc_status_id_fkey,
  ADD CONSTRAINT customer_account_doc_status_id_fkey FOREIGN KEY (doc_status_id) REFERENCES customer.status ON DELETE SET NULL;
ALTER TABLE customer.customer_account DROP CONSTRAINT customer_account_status_id_fkey,
  ADD CONSTRAINT customer_account_status_id_fkey FOREIGN KEY (status_id) REFERENCES customer.status ON DELETE SET NULL;
ALTER TABLE customer.customer_account DROP CONSTRAINT customer_account_contact_info_id_fkey,
  ADD CONSTRAINT customer_account_contact_info_id_fkey FOREIGN KEY (contact_info_id) REFERENCES customer.organisation ON DELETE SET NULL;

-- add in one ON DELETE CASCADE that was missed
ALTER TABLE customer.account_notification DROP CONSTRAINT account_notification_customer_account_id_fkey,
  ADD CONSTRAINT account_notification_customer_account_id_fkey FOREIGN KEY (customer_account_id) REFERENCES customer.customer_account ON DELETE CASCADE;
