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

public enum TimeAttributeKind {
	NONE(0),
	TEN_MINUTE(1),
	FIFTEEN_MINUTE(2),
	ONE_MINUTE(3),
	TWENTY_FOUR_HOUR(4),
	THIRTY_MINUTE(5),
	FIVE_MINUTE(6),
	SIXTY_MINUTE(7),
	TWO_MINUTE(10),
	THREE_MINUTE(14),
	PRESENT(15),
	PREVIOUS(16),
	TWENTY_MINUTE(31),
	FIXED_BLOCK_60_MIN(50),
	FIXED_BLOCK_30_MIN(51),
	FIXED_BLOCK_20_MIN(52),
	FIXED_BLOCK_15_MIN(53),
	FIXED_BLOCK_10_MIN(54),
	FIXED_BLOCK_5_MIN(55),
	FIXED_BLOCK_1_MIN(56),
	ROLLING_BLOCK_60_MIN_INTVL_30_MIN_SUB_INTVL(57),
	ROLLING_BLOCK_60_MIN_INTVL_20_MIN_SUB_INTVL(58),
	ROLLING_BLOCK_60_MIN_INTVL_15_MIN_SUB_INTVL(59),
	ROLLING_BLOCK_60_MIN_INTVL_12_MIN_SUB_INTVL(60),
	ROLLING_BLOCK_60_MIN_INTVL_10_MIN_SUB_INTVL(61),
	ROLLING_BLOCK_60_MIN_INTVL_6_MIN_SUB_INTVL(62),
	ROLLING_BLOCK_60_MIN_INTVL_5_MIN_SUB_INTVL(63),
	ROLLING_BLOCK_60_MIN_INTVL_4_MIN_SUB_INTVL(64),
	ROLLING_BLOCK_30_MIN_INTVL_15_MIN_SUB_INTVL(65),
	ROLLING_BLOCK_30_MIN_INTVL_10_MIN_SUB_INTVL(66),
	ROLLING_BLOCK_30_MIN_INTVL_6_MIN_SUB_INTVL(67),
	ROLLING_BLOCK_30_MIN_INTVL_5_MIN_SUB_INTVL(68),
	ROLLING_BLOCK_30_MIN_INTVL_3_MIN_SUB_INTVL(69),
	ROLLING_BLOCK_30_MIN_INTVL_2_MIN_SUB_INTVL(70),
	ROLLING_BLOCK_15_MIN_INTVL_5_MIN_SUB_INTVL(71),
	ROLLING_BLOCK_15_MIN_INTVL_3_MIN_SUB_INTVL(72),
	ROLLING_BLOCK_15_MIN_INTVL_1_MIN_SUB_INTVL(73),
	ROLLING_BLOCK_10_MIN_INTVL_5_MIN_SUB_INTVL(74),
	ROLLING_BLOCK_10_MIN_INTVL_2_MIN_SUB_INTVL(75),
	ROLLING_BLOCK_10_MIN_INTVL_1_MIN_SUB_INTVL(76),
	ROLLING_BLOCK_5_MIN_INTVL_1_MIN_SUB_INTVL(77);

	public final int schemaValue;

	TimeAttributeKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static TimeAttributeKind getTimeAttributeKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(TimeAttributeKind.class).stream()
			.filter(tak -> tak.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + TimeAttributeKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
