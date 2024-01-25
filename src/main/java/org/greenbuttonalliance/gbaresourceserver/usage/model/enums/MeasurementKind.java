/*
 * Copyright (c) 2022-2024 Green Button Alliance, Inc.
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

package org.greenbuttonalliance.gbaresourceserver.usage.model.enums;

import java.util.EnumSet;

public enum MeasurementKind {
	NONE(0),
	APPARENT_POWER_FACTOR(2),
	CURRENCY(3),
	CURRENT(4),
	CURRENT_ANGLE(5),
	CURRENT_IMBALANCE(6),
	DATE(7),
	DEMAND(8),
	DISTANCE(9),
	DISTORTION_VOLT_AMPERES(10),
	ENERGIZATION(11),
	ENERGY(12),
	ENERGIZATION_LOAD_SIDE(13),
	FAN(14),
	FREQUENCY(15),
	FUNDS(16),
	IEEE_1366_ASAI(17),
	IEEE_1366_ASIDI(18),
	IEEE_1366_ASIFI(19),
	IEEE_1366_CAIDI(20),
	IEEE_1366_CAIFI(21),
	IEEE_1366_CEMI_N(22),
	IEEE_1366_CEMSMI_N(23),
	IEEE_1366_CTAIDI(24),
	IEEE_1366_MAIFI(25),
	IEEE_1366_MAIFI_E(26),
	IEEE_1366_SAIDI(27),
	IEEE_1366_SAIFI(28),
	LINE_LOSSES(31),
	LOSSES(32),
	NEGATIVE_SEQUENCE(33),
	PHASOR_POWER_FACTOR(34),
	PHASOR_REACTIVE_POWER(35),
	POSITIVE_SEQUENCE(36),
	POWER(37),
	POWER_FACTOR(38),
	QUANTITY_POWER(40),
	SAG(41),
	SWELL(42),
	SWITCH_POSITION(43),
	TAP_POSITION(44),
	TARIFF_RATE(45),
	TEMPERATURE(46),
	TOTAL_HARMONIC_DISTORTION(47),
	TRANSFORMER_LOSSES(48),
	UNIPEDE_VOLTAGE_DIP_10_TO_15(49),
	UNIPEDE_VOLTAGE_DIP_15_TO_30(50),
	UNIPEDE_VOLTAGE_DIP_30_TO_60(51),
	UNIPEDE_VOLTAGE_DIP_60_TO_90(52),
	UNIPEDE_VOLTAGE_DIP_90_TO_100(53),
	VOLTAGE(54),
	VOLTAGE_ANGLE(55),
	VOLTAGE_EXCURSION(56),
	VOLTAGE_IMBALANCE(57),
	VOLUME(58),
	ZERO_FLOW_DURATION(59),
	ZERO_SEQUENCE(60),
	DISTORTION_POWER_FACTOR(64),
	FREQUENCY_EXCURSION(81),
	APPLICATION_CONTEXT(90),
	AP_TITLE(91),
	ASSET_NUMBER(92),
	BANDWIDTH(93),
	BATTERY_VOLTAGE(94),
	BROADCAST_ADDRESS(95),
	DEVICE_ADDRESS_TYPE_1(96),
	DEVICE_ADDRESS_TYPE_2(97),
	DEVICE_ADDRESS_TYPE_3(98),
	DEVICE_ADDRESS_TYPE_4(99),
	DEVICE_CLAS(100),
	ELECTRONIC_SERIAL_NUMBER(101),
	END_DEVICE_ID(102),
	GROUP_ADDRESS_TYPE_1(103),
	GROUP_ADDRESS_TYPE_2(104),
	GROUP_ADDRESS_TYPE_3(105),
	GROUP_ADDRESS_TYPE_4(106),
	IP_ADDRESS(107),
	MAC_ADDRESS(108),
	MFG_ASSIGNED_CONFIGURATION_ID(109),
	MFG_ASSIGNED_PHYSICAL_SERIAL_NUMBER(110),
	MFG_ASSIGNED_PRODUCT_NUMBER(111),
	MFG_ASSIGNED_UNIQUE_COMMUNICATION_ADDRESS(112),
	MULTICAST_ADDRESS(113),
	ONE_WAY_ADDRESS(114),
	SIGNAL_STRENGTH(115),
	TWO_WAY_ADDRESS(116),
	SIGNAL_TO_NOISE_RATIO(117),
	ALARM(118),
	BATTERY_CARRYOVER(119),
	DATA_OVERFLOW_ALARM(120),
	DEMAND_LIMIT(121),
	DEMAND_RESET(112),
	DIAGNOSTIC(123),
	EMERGENCY_LIMIT(124),
	ENCODER_TAMPER(125),
	IEEE_1366_MOMENTARY_INTERRUPTION(126),
	IEEE_1366_MOMENTARY_INTERRUPTION_EVENT(127),
	IEEE_1366_SUSTAINED_INTERRUPTION(128),
	INTERRUPTION_BEHAVIOUR(129),
	INVERSION_TAMPER(130),
	LOAD_INTERRUPT(131),
	LOAD_SHED(132),
	MAINTENANCE(133),
	PHYSICAL_TAMPER(134),
	POWER_LOSS_TAMPER(135),
	POWER_OUTAGE(136),
	POWER_QUALITY(137),
	POWER_RESTORATION(138),
	PROGRAMMED(139),
	PUSH_BUTTON(140),
	RELAY_ACTIVATION(141),
	RELAY_CYCLE(142),
	REMOVAL_TAMPER(143),
	REPROGRAMMING_TAMPER(144),
	REVERSE_ROTATION_TAMPER(145),
	SWITCH_ARMED(146),
	SWITCH_DISABLED(147),
	TAMPER(148),
	WATCHDOG_TIMEOUT(149),
	BILL_LAST_PERIOD(150),
	BILL_TO_DATE(151),
	BILL_CARRYOVER(152),
	CONNECTION_FEE(153),
	AUDIBLE_VOLUME(154),
	VOLUMETRIC_FLOW(155);

	public final int schemaValue;

	MeasurementKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static MeasurementKind getMeasurementKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(MeasurementKind.class).stream()
			.filter(mk -> mk.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + MeasurementKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
