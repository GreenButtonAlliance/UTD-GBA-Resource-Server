/*
 * Copyright (c) 2023 Green Button Alliance, Inc.
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

-- Drop Usage database tables
DROP TABLE IF EXISTS usage.aggregate_node_ref CASCADE;
DROP TABLE IF EXISTS usage.application_information CASCADE;
DROP TABLE IF EXISTS usage.application_information_contact CASCADE;
DROP TABLE IF EXISTS usage.application_information_grant_type CASCADE;
DROP TABLE IF EXISTS usage.application_information_redirect_uri CASCADE;
DROP TABLE IF EXISTS usage.application_information_scope CASCADE;
DROP TABLE IF EXISTS usage.authorization CASCADE;
DROP TABLE IF EXISTS usage.electric_power_quality_summaries CASCADE;
DROP TABLE IF EXISTS usage.interval_block CASCADE;
DROP TABLE IF EXISTS usage.interval_reading CASCADE;
DROP TABLE IF EXISTS usage.line_item CASCADE;
DROP TABLE IF EXISTS usage.meter_reading CASCADE;
DROP TABLE IF EXISTS usage.pnode_ref CASCADE;
DROP TABLE IF EXISTS usage.reading_quality CASCADE;
DROP TABLE IF EXISTS usage.reading_type CASCADE;
DROP TABLE IF EXISTS usage.retail_customer CASCADE;
DROP TABLE IF EXISTS usage.service_delivery_point CASCADE;
DROP TABLE IF EXISTS usage.service_delivery_point_tariff_rider_ref CASCADE;
DROP TABLE IF EXISTS usage.service_delivery_points CASCADE;
DROP TABLE IF EXISTS usage.subscription CASCADE;
DROP TABLE IF EXISTS usage.tariff_rider_ref CASCADE;
DROP TABLE IF EXISTS usage.time_configuration CASCADE;
DROP TABLE IF EXISTS usage.usage_point CASCADE;
DROP TABLE IF EXISTS usage.usage_point_aggregate_node_ref CASCADE;
DROP TABLE IF EXISTS usage.usage_point_pnode_ref CASCADE;
DROP TABLE IF EXISTS usage.usage_summaries CASCADE;
DROP TABLE IF EXISTS usage.usage_summaries_tariff_rider_ref CASCADE;

-- Drop Usage types
DROP TYPE IF EXISTS usage.accumulation_kind CASCADE;
DROP TYPE IF EXISTS usage.am_i_billing_ready_kind CASCADE;
DROP TYPE IF EXISTS usage.anode_type CASCADE;
DROP TYPE IF EXISTS usage.apnode_type CASCADE;
DROP TYPE IF EXISTS usage.commodity_kind CASCADE;
DROP TYPE IF EXISTS usage.currency CASCADE;
DROP TYPE IF EXISTS usage.data_custodian_application_status CASCADE;
DROP TYPE IF EXISTS usage.data_qualifier_kind CASCADE;
DROP TYPE IF EXISTS usage.enrollment_status CASCADE;
DROP TYPE IF EXISTS usage.flow_direction_kind CASCADE;
DROP TYPE IF EXISTS usage.grant_type CASCADE;
DROP TYPE IF EXISTS usage.item_kind CASCADE;
DROP TYPE IF EXISTS usage.measurement_kind CASCADE;
DROP TYPE IF EXISTS usage.o_auth_error CASCADE;
DROP TYPE IF EXISTS usage.phase_code_kind CASCADE;
DROP TYPE IF EXISTS usage.service_kind CASCADE;
DROP TYPE IF EXISTS usage.third_party_application_status CASCADE;
DROP TYPE IF EXISTS usage.third_party_application_type CASCADE;
DROP TYPE IF EXISTS usage.third_party_application_use CASCADE;
DROP TYPE IF EXISTS usage.time_attribute_kind CASCADE;
DROP TYPE IF EXISTS usage.time_period_of_interest CASCADE;
DROP TYPE IF EXISTS usage.token_endpoint_method CASCADE;
DROP TYPE IF EXISTS usage.unit_multiplier_kind CASCADE;
DROP TYPE IF EXISTS usage.unit_symbol_kind CASCADE;
DROP TYPE IF EXISTS usage.usage_point_connected_kind CASCADE;

-- Drop Retail Customer tables
DROP TABLE IF EXISTS customer.account_notification CASCADE;
DROP TABLE IF EXISTS customer.customer CASCADE;
DROP TABLE IF EXISTS customer.customer_account CASCADE;
DROP TABLE IF EXISTS customer.customer_agreement CASCADE;
DROP TABLE IF EXISTS customer.customer_agreement_future_status CASCADE;
DROP TABLE IF EXISTS customer.customer_agreement_pricing_structure CASCADE;
DROP TABLE IF EXISTS customer.demand_response_program CASCADE;
DROP TABLE IF EXISTS customer.electronic_address CASCADE;
DROP TABLE IF EXISTS customer.end_device CASCADE;
DROP TABLE IF EXISTS customer.organisation CASCADE;
DROP TABLE IF EXISTS customer.priority CASCADE;
DROP TABLE IF EXISTS customer.program_date CASCADE;
DROP TABLE IF EXISTS customer.service_location CASCADE;
DROP TABLE IF EXISTS customer.service_supplier CASCADE;
DROP TABLE IF EXISTS customer.status CASCADE;
DROP TABLE IF EXISTS customer.street_address CASCADE;
DROP TABLE IF EXISTS customer.telephone_number CASCADE;
DROP TABLE IF EXISTS customer.usage_points CASCADE;

-- Drop Retail Customer types
DROP TYPE IF EXISTS customer.customer_kind CASCADE;
DROP TYPE IF EXISTS customer.notification_method_kind CASCADE;
DROP TYPE IF EXISTS customer.supplier_kind CASCADE;

-- Drop Postgres Public types
DROP TYPE IF EXISTS public.currency CASCADE;
DROP TYPE IF EXISTS public.unit_multiplier_kind CASCADE;
DROP TYPE IF EXISTS public.unit_symbol_kind CASCADE;
DROP TYPE IF EXISTS public.enrollment_status CASCADE;

-- Clear Flyway Schema History table
DELETE FROM flyway_schema_history WHERE version > '0.0';
