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

public enum TimePeriodOfInterest {
	NONE(0),
	BILLING_PERIOD(8),
	DAILY(11),
	MONTHLY(13),
	SEASONAL(22),
	WEEKLY(24),
	SPECIFIED_PERIOD(32);

	public final int schemaValue;

	TimePeriodOfInterest(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static TimePeriodOfInterest getTimePeriodFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(TimePeriodOfInterest.class).stream()
			.filter(tpoi -> tpoi.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + TimePeriodOfInterest.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
