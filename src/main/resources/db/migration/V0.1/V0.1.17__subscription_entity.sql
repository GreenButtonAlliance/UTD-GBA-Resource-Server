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

CREATE TABLE IF NOT EXISTS usage.subscription (
                                                uuid UUID PRIMARY KEY,
                                                description TEXT,
                                                published TIMESTAMP,
                                                self_link_href TEXT,
                                                up_link_href TEXT,
                                                updated TIMESTAMP,
                                                hashed_id TEXT,
                                                last_update TIMESTAMP,
                                                application_information_uuid UUID REFERENCES usage
                                                  .application_information,
                                                authorization_id UUID REFERENCES usage.authorization,
                                                retail_customer_id           UUID REFERENCES usage.retail_customer
--                                                 usage_point_uuid UUID REFERENCES usage.usage_points
);

DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM pg_constraint WHERE conname = 'fk_subscription') THEN
      ALTER TABLE usage.authorization ADD CONSTRAINT fk_subscription FOREIGN KEY (subscription_id) REFERENCES
        usage.subscription;
    END IF;
  END $$;
