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

public enum FlowDirectionKind {
	NONE(0),
	FORWARD(1),
	LAGGING(2),
	LEADING(3),
	NET(4),
	Q1_PLUS_Q2(5),
	Q1_PLUS_Q3(7),
	Q1_PLUS_Q4(8),
	Q1_MINUS_Q4(9),
	Q2_PLUS_Q3(10),
	Q2_PLUS_Q4(11),
	Q2_MINUS_Q3(12),
	Q3_PLUS_Q4(13),
	Q3_MINUS_Q2(14),
	QUADRANT_1(15),
	QUADRANT_2(16),
	QUADRANT_3(17),
	QUADRANT_4(18),
	REVERSE(19),
	TOTAL(20),
	TOTAL_BY_PHASE(21);

	public final int schemaValue;

	FlowDirectionKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static FlowDirectionKind getFlowDirectionKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(FlowDirectionKind.class).stream()
			.filter(fdk -> fdk.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + FlowDirectionKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
