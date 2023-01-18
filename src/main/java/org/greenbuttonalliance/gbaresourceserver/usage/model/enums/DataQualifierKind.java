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

public enum DataQualifierKind {
	NONE(0),
	AVERAGE(2),
	EXCESS(4),
	HIGH_THRESHOLD(5),
	LOW_THRESHOLD(7),
	MAXIMUM(8),
	MINIMUM(9),
	NOMINAL(11),
	NORMAL(12),
	SECOND_MAXIMUM(16),
	SECOND_MINIMUM(17),
	THIRD_MAXIMUM(23),
	FOURTH_MAXIMUM(24),
	FIFTH_MAXIMUM(25),
	SUM(26);

	public final int schemaValue;

	DataQualifierKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static DataQualifierKind getDataQualifierKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(DataQualifierKind.class).stream()
			.filter(dqk -> dqk.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + DataQualifierKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
