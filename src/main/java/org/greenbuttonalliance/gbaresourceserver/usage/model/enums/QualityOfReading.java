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

public enum QualityOfReading {
	VALID(0),
	MANUALLY_EDITED(7),
	ESTIMATED_USING_REFERENCE_DAY(8),
	ESTIMATED_USING_LINEAR_INTERPOLATION(9),
	QUESTIONABLE(10),
	DERIVED(11),
	PROJECTED_FORECAST(12),
	MIXED(13),
	RAW(14),
	NORMALIZED_FOR_WEATHER(15),
	OTHER(16),
	VALIDATED(17),
	VERIFIED(18),
	REVENUE_QUALITY(19);

	public final int schemaValue;

	QualityOfReading(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static QualityOfReading getQualityFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(QualityOfReading.class).stream()
			.filter(qor -> qor.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + QualityOfReading.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
