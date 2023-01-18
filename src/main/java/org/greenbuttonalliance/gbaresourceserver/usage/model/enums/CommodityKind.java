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

package org.greenbuttonalliance.gbaresourceserver.usage.model.enums;

import java.util.EnumSet;

public enum CommodityKind {
	NONE(0),
	ELECTRICITY_SECONDARY_METERED(1),
	ELECTRICITY_PRIMARY_METERED(2),
	COMMUNICATION(3),
	AIR(4),
	INSULATIVE_GAS(5),
	INSULATIVE_OIL(6),
	NATURAL_GAS(7),
	PROPANE(8),
	POTABLE_WATER(9),
	STEAM(10),
	WASTE_WATER(11),
	HEATING_FLUID(12),
	COOLING_FLUID(13),
	NON_POTABLE_WATER(14),
	NOX(15),
	SO2(16),
	CH4(17),
	CO2(18),
	CARBON(19),
	HCH(20),
	PFC(21),
	SF6(22),
	TV_LICENSE(23),
	INTERNET(24),
	REFUSE(25),
	ELECTRICITY_TRANSMISSION_METERED(26);

	public final int schemaValue;

	CommodityKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static CommodityKind getCommodityKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(CommodityKind.class).stream()
			.filter(ck -> ck.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + CommodityKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
