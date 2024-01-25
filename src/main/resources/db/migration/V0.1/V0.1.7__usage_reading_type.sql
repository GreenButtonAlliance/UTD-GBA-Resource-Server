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

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'accumulation_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.accumulation_kind AS ENUM (
      'NONE',
      'BULK_QUANTITY',
      'CONTINUOUS_CUMULATIVE',
      'CUMULATIVE',
      'DELTA_DATA',
      'INDICATING',
      'SUMMATION',
      'TIME_DELAY',
      'INSTANTANEOUS',
      'LATCHING_QUANTITY',
      'BOUNDED_QUANTITY'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'commodity_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.commodity_kind AS ENUM (
      'NONE',
      'ELECTRICITY_SECONDARY_METERED',
      'ELECTRICITY_PRIMARY_METERED',
      'COMMUNICATION',
      'AIR',
      'INSULATIVE_GAS',
      'INSULATIVE_OIL',
      'NATURAL_GAS',
      'PROPANE',
      'POTABLE_WATER',
      'STEAM',
      'WASTE_WATER',
      'HEATING_FLUID',
      'COOLING_FLUID',
      'NON_POTABLE_WATER',
      'NOX',
      'SO2',
      'CH4',
      'CO2',
      'CARBON',
      'HCH',
      'PFC',
      'SF6',
      'TV_LICENSE',
      'INTERNET',
      'REFUSE',
      'ELECTRICITY_TRANSMISSION_METERED'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'currency' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.currency AS ENUM (
      'USD',
      'EUR',
      'AUD',
      'CAD',
      'CHF',
      'CNY',
      'DKK',
      'GBP',
      'JPY',
      'NOK',
      'RUB',
      'SEK',
      'INR',
      'OTHER'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'data_qualifier_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.data_qualifier_kind AS ENUM (
      'NONE',
      'AVERAGE',
      'EXCESS',
      'HIGH_THRESHOLD',
      'LOW_THRESHOLD',
      'MAXIMUM',
      'MINIMUM',
      'NOMINAL',
      'NORMAL',
      'SECOND_MAXIMUM',
      'SECOND_MINIMUM',
      'THIRD_MAXIMUM',
      'FOURTH_MAXIMUM',
      'FIFTH_MAXIMUM',
      'SUM'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'flow_direction_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.flow_direction_kind AS ENUM (
      'NONE',
      'FORWARD',
      'LAGGING',
      'LEADING',
      'NET',
      'Q1_PLUS_Q2',
      'Q1_PLUS_Q3',
      'Q1_PLUS_Q4',
      'Q1_MINUS_Q4',
      'Q2_PLUS_Q3',
      'Q2_PLUS_Q4',
      'Q2_MINUS_Q3',
      'Q3_PLUS_Q4',
      'Q3_MINUS_Q2',
      'QUADRANT_1',
      'QUADRANT_2',
      'QUADRANT_3',
      'QUADRANT_4',
      'REVERSE',
      'TOTAL',
      'TOTAL_BY_PHASE'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'measurement_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.measurement_kind AS ENUM (
      'NONE',
      'APPARENT_POWER_FACTOR',
      'CURRENCY',
      'CURRENT',
      'CURRENT_ANGLE',
      'CURRENT_IMBALANCE',
      'DATE',
      'DEMAND',
      'DISTANCE',
      'DISTORTION_VOLT_AMPERES',
      'ENERGIZATION',
      'ENERGY',
      'ENERGIZATION_LOAD_SIDE',
      'FAN',
      'FREQUENCY',
      'FUNDS',
      'IEEE_1366_ASAI',
      'IEEE_1366_ASIDI',
      'IEEE_1366_ASIFI',
      'IEEE_1366_CAIDI',
      'IEEE_1366_CAIFI',
      'IEEE_1366_CEMI_N',
      'IEEE_1366_CEMSMI_N',
      'IEEE_1366_CTAIDI',
      'IEEE_1366_MAIFI',
      'IEEE_1366_MAIFI_E',
      'IEEE_1366_SAIDI',
      'IEEE_1366_SAIFI',
      'LINE_LOSSES',
      'LOSSES',
      'NEGATIVE_SEQUENCE',
      'PHASOR_POWER_FACTOR',
      'PHASOR_REACTIVE_POWER',
      'POSITIVE_SEQUENCE',
      'POWER',
      'POWER_FACTOR',
      'QUANTITY_POWER',
      'SAG',
      'SWELL',
      'SWITCH_POSITION',
      'TAP_POSITION',
      'TARIFF_RATE',
      'TEMPERATURE',
      'TOTAL_HARMONIC_DISTORTION',
      'TRANSFORMER_LOSSES',
      'UNIPEDE_VOLTAGE_DIP_10_TO_15',
      'UNIPEDE_VOLTAGE_DIP_15_TO_30',
      'UNIPEDE_VOLTAGE_DIP_30_TO_60',
      'UNIPEDE_VOLTAGE_DIP_60_TO_90',
      'UNIPEDE_VOLTAGE_DIP_90_TO_100',
      'VOLTAGE',
      'VOLTAGE_ANGLE',
      'VOLTAGE_EXCURSION',
      'VOLTAGE_IMBALANCE',
      'VOLUME',
      'ZERO_FLOW_DURATION',
      'ZERO_SEQUENCE',
      'DISTORTION_POWER_FACTOR',
      'FREQUENCY_EXCURSION',
      'APPLICATION_CONTEXT',
      'AP_TITLE',
      'ASSET_NUMBER',
      'BANDWIDTH',
      'BATTERY_VOLTAGE',
      'BROADCAST_ADDRESS',
      'DEVICE_ADDRESS_TYPE_1',
      'DEVICE_ADDRESS_TYPE_2',
      'DEVICE_ADDRESS_TYPE_3',
      'DEVICE_ADDRESS_TYPE_4',
      'DEVICE_CLAS',
      'ELECTRONIC_SERIAL_NUMBER',
      'END_DEVICE_ID',
      'GROUP_ADDRESS_TYPE_1',
      'GROUP_ADDRESS_TYPE_2',
      'GROUP_ADDRESS_TYPE_3',
      'GROUP_ADDRESS_TYPE_4',
      'IP_ADDRESS',
      'MAC_ADDRESS',
      'MFG_ASSIGNED_CONFIGURATION_ID',
      'MFG_ASSIGNED_PHYSICAL_SERIAL_NUMBER',
      'MFG_ASSIGNED_PRODUCT_NUMBER',
      'MFG_ASSIGNED_UNIQUE_COMMUNICATION_ADDRESS',
      'MULTICAST_ADDRESS',
      'ONE_WAY_ADDRESS',
      'SIGNAL_STRENGTH',
      'TWO_WAY_ADDRESS',
      'SIGNAL_TO_NOISE_RATIO',
      'ALARM',
      'BATTERY_CARRYOVER',
      'DATA_OVERFLOW_ALARM',
      'DEMAND_LIMIT',
      'DEMAND_RESET',
      'DIAGNOSTIC',
      'EMERGENCY_LIMIT',
      'ENCODER_TAMPER',
      'IEEE_1366_MOMENTARY_INTERRUPTION',
      'IEEE_1366_MOMENTARY_INTERRUPTION_EVENT',
      'IEEE_1366_SUSTAINED_INTERRUPTION',
      'INTERRUPTION_BEHAVIOUR',
      'INVERSION_TAMPER',
      'LOAD_INTERRUPT',
      'LOAD_SHED',
      'MAINTENANCE',
      'PHYSICAL_TAMPER',
      'POWER_LOSS_TAMPER',
      'POWER_OUTAGE',
      'POWER_QUALITY',
      'POWER_RESTORATION',
      'PROGRAMMED',
      'PUSH_BUTTON',
      'RELAY_ACTIVATION',
      'RELAY_CYCLE',
      'REMOVAL_TAMPER',
      'REPROGRAMMING_TAMPER',
      'REVERSE_ROTATION_TAMPER',
      'SWITCH_ARMED',
      'SWITCH_DISABLED',
      'TAMPER',
      'WATCHDOG_TIMEOUT',
      'BILL_LAST_PERIOD',
      'BILL_TO_DATE',
      'BILL_CARRYOVER',
      'CONNECTION_FEE',
      'AUDIBLE_VOLUME',
      'VOLUMETRIC_FLOW'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'phase_code_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.phase_code_kind AS ENUM (
      'ABCN',
      'ABC',
      'ABN',
      'ACN',
      'BCN',
      'AB',
      'AC',
      'BC',
      'AN',
      'BN',
      'CN',
      'A',
      'B',
      'C',
      'N',
      'S2N',
      'S12NEUTRAL',
      'S1N',
      'S2',
      'S12',
      'S12N',
      'NONE',
      'A_TO_A_V',
      'BA_V',
      'CA_V',
      'NG',
      'S1'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'unit_multiplier_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.unit_multiplier_kind AS ENUM (
      'PICO',
      'NANO',
      'MICRO',
      'MILLI',
      'CENTI',
      'DECI',
      'KILO',
      'MEGA',
      'GIGA',
      'TERA',
      'NONE',
      'DECA',
      'HECTO'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'time_period_of_interest' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.time_period_of_interest AS ENUM (
      'NONE',
      'BILLING_PERIOD',
      'DAILY',
      'MONTHLY',
      'SEASONAL',
      'WEEKLY',
      'SPECIFIED_PERIOD'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'unit_symbol_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.unit_symbol_kind AS ENUM (
      'V_A',
      'W',
      'V_A_R',
      'V_A_H',
      'W_H',
      'V_A_RH',
      'V',
      'OHM',
      'A',
      'F',
      'HEN',
      'DEG_C',
      'SEC',
      'MIN',
      'H',
      'DEG',
      'RAD',
      'J',
      'N',
      'SIEMENS',
      'NONE',
      'HZ',
      'G',
      'PA',
      'M',
      'M2',
      'M3',
      'A2',
      'A2_H',
      'A2_S',
      'A_H',
      'A_PER_A',
      'A_PER_M',
      'A_S',
      'B',
      'B_M',
      'BQ',
      'BTU',
      'BTU_PER_H',
      'CD',
      'CHAR',
      'HZ_PER_SEC',
      'CODE',
      'COS_THETA',
      'COUNT',
      'FT3',
      'FT3_COMPENSATED',
      'FT3_COMPENSATED_PER_H',
      'G_M2',
      'G_PER_G',
      'GY',
      'HZ_PER_HZ',
      'CHAR_PER_SEC',
      'IMPERIAL_GAL',
      'IMPERIAL_GAL_PER_H',
      'J_PER_K',
      'J_PER_KG',
      'K',
      'KAT',
      'KG_M',
      'KG_PER_M3',
      'LITRE',
      'LITRE_COMPENSATED',
      'LITRE_COMPENSATED_PER_H',
      'LITRE_PER_H',
      'LITRE_PER_LITRE',
      'LITRE_PER_SEC',
      'LITRE_UNCOMPENSATED',
      'LITRE_UNCOMPENSATED_PER_H',
      'LM',
      'LX',
      'M2_PER_SEC',
      'M3_COMPENSATED',
      'M3_COMPENSATED_PER_H',
      'M3_PER_H',
      'M3_PER_SEC',
      'M3_UNCOMPENSATED',
      'M3_UNCOMPENSATED_PER_H',
      'ME_CODE',
      'MOL',
      'MOL_PER_KG',
      'MOL_PER_M3',
      'MOL_PER_MOL',
      'MONEY',
      'M_PER_M',
      'M_PER_M3',
      'M_PER_SEC',
      'M_PER_SEC2',
      'OHM_M',
      'PA_A',
      'PA_G',
      'PSI_A',
      'PSI_G',
      'Q',
      'Q45',
      'Q45_H',
      'Q60',
      'Q60_H',
      'Q_H',
      'RAD_PER_SEC',
      'REV',
      'REV_PER_SEC',
      'SEC_PER_SEC',
      'SR',
      'STATUS',
      'SV',
      'T',
      'THERM',
      'TIMESTAMP',
      'US_GAL',
      'US_GAL_PER_H',
      'V2',
      'V2_H',
      'V_A_H_PER_REV',
      'V_A_RH_PER_REV',
      'V_PER_HZ',
      'V_PER_V',
      'V_SEC',
      'WB',
      'W_H_PER_M3',
      'W_H_PER_REV',
      'W_PER_M_K',
      'W_PER_SEC',
      'W_PER_V_A',
      'W_PER_W'
    );
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_type t INNER JOIN pg_namespace ns ON t.typnamespace = ns.oid WHERE t.typname = 'time_attribute_kind' AND ns.nspname = 'usage') THEN
    CREATE TYPE usage.time_attribute_kind AS ENUM (
      'NONE',
      'TEN_MINUTE',
      'FIFTEEN_MINUTE',
      'ONE_MINUTE',
      'TWENTY_FOUR_HOUR',
      'THIRTY_MINUTE',
      'FIVE_MINUTE',
      'SIXTY_MINUTE',
      'TWO_MINUTE',
      'THREE_MINUTE',
      'PRESENT',
      'PREVIOUS',
      'TWENTY_MINUTE',
      'FIXED_BLOCK_60_MIN',
      'FIXED_BLOCK_30_MIN',
      'FIXED_BLOCK_20_MIN',
      'FIXED_BLOCK_15_MIN',
      'FIXED_BLOCK_10_MIN',
      'FIXED_BLOCK_5_MIN',
      'FIXED_BLOCK_1_MIN',
      'ROLLING_BLOCK_60_MIN_INTVL_30_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_20_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_15_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_12_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_10_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_6_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_5_MIN_SUB_INTVL',
      'ROLLING_BLOCK_60_MIN_INTVL_4_MIN_SUB_INTVL',
      'ROLLING_BLOCK_30_MIN_INTVL_15_MIN_SUB_INTVL',
      'ROLLING_BLOCK_30_MIN_INTVL_10_MIN_SUB_INTVL',
      'ROLLING_BLOCK_30_MIN_INTVL_6_MIN_SUB_INTVL',
      'ROLLING_BLOCK_30_MIN_INTVL_5_MIN_SUB_INTVL',
      'ROLLING_BLOCK_30_MIN_INTVL_3_MIN_SUB_INTVL',
      'ROLLING_BLOCK_30_MIN_INTVL_2_MIN_SUB_INTVL',
      'ROLLING_BLOCK_15_MIN_INTVL_5_MIN_SUB_INTVL',
      'ROLLING_BLOCK_15_MIN_INTVL_3_MIN_SUB_INTVL',
      'ROLLING_BLOCK_15_MIN_INTVL_1_MIN_SUB_INTVL',
      'ROLLING_BLOCK_10_MIN_INTVL_5_MIN_SUB_INTVL',
      'ROLLING_BLOCK_10_MIN_INTVL_2_MIN_SUB_INTVL',
      'ROLLING_BLOCK_10_MIN_INTVL_1_MIN_SUB_INTVL',
      'ROLLING_BLOCK_5_MIN_INTVL_1_MIN_SUB_INTVL'
    );
  END IF;
END $$;

CREATE TABLE IF NOT EXISTS usage.reading_type (
  uuid UUID PRIMARY KEY,
  description TEXT,
  published TIMESTAMP,
  self_link_href TEXT,
  up_link_href TEXT,
  updated TIMESTAMP,
  accumulation_behavior usage.accumulation_kind,
  commodity usage.commodity_kind,
  consumption_tier SMALLINT,
  currency usage.currency,
  data_qualifier usage.data_qualifier_kind,
  default_quality usage.quality_of_reading,
  flow_direction usage.flow_direction_kind,
  interval_length BIGINT,
  kind usage.measurement_kind,
  phase usage.phase_code_kind,
  power_of_ten_multiplier usage.unit_multiplier_kind,
  time_attribute usage.time_period_of_interest,
  tou SMALLINT,
  uom usage.unit_symbol_kind,
  cpp SMALLINT,
  interharmonic_numerator BIGINT,
  interharmonic_denominator BIGINT,
  measuring_period usage.time_attribute_kind,
  argument_numerator BIGINT,
  argument_denominator BIGINT
);
