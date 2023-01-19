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

public enum AccumulationKind {
	NONE(0),
	BULK_QUANTITY(1),
	CONTINUOUS_CUMULATIVE(2),
	CUMULATIVE(3),
	DELTA_DATA(4),
	INDICATING(6),
	SUMMATION(9),
	TIME_DELAY(10),
	INSTANTANEOUS(12),
	LATCHING_QUANTITY(13),
	BOUNDED_QUANTITY(14);

	public final int schemaValue;

	AccumulationKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static AccumulationKind getAccumulationKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(AccumulationKind.class).stream()
			.filter(ak -> ak.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + AccumulationKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
