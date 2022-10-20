/*
 * Copyright (c) 2022 Green Button Alliance, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.usage.model.enums;

import java.util.EnumSet;

public enum UnitMultiplierKind {
	PICO((short)-12),
	NANO((short)-9),
	MICRO((short)-6),
	MILLI((short)-3),
	CENTI((short)-2),
	DECI((short)-1),
	KILO((short)3),
	MEGA((short)6),
	GIGA((short)9),
	TERA((short)12),
	NONE((short)0),
	DECA((short)1),
	HECTO((short)2);

	public final short schemaValue;

	UnitMultiplierKind(short schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static UnitMultiplierKind getUnitMultiplierKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(UnitMultiplierKind.class).stream()
			.filter(umk -> umk.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + UnitMultiplierKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
