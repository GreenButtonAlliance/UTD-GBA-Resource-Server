/*
 *
 *  * Copyright (c) 2022 Green Button Alliance, Inc.
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package org.greenbuttonalliance.gbaresourceserver.usage.model.enums;
import java.util.EnumSet;
public enum ThirdPartyApplicationUse {
	ENERGYMANAGEMENT(1),
	COMPARISONS(2),
	GOVERNMENT(3),
	ACADEMIC(4),
	LAWENFORCEMENT(5);
public final int schemaValue;
	ThirdPartyApplicationUse(int schemaValue) {
	this.schemaValue = schemaValue;
	}
public static ThirdPartyApplicationUse getAccumulationKindFromSchemaValue(int schemaValue) {
	return EnumSet.allOf(ThirdPartyApplicationUse.class).stream()
	.filter(ak -> ak.schemaValue == schemaValue)
	.findFirst()
	.orElseThrow(() -> new IllegalArgumentException("No " + AccumulationKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
