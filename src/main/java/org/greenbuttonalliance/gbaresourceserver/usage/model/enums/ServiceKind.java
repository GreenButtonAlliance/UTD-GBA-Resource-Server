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

public enum ServiceKind {
	ELECTRICITY(0),
	GAS(1),
	WATER(2),
	TIME(3),
	HEAT(4),
	REFUSE(5),
	SEWERAGE(6),
	RATES(7),
	TVLICENSE(8),
	INTERNET(9);

	public final int schemaValue;

	ServiceKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static ServiceKind getServiceKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(ServiceKind.class).stream()
			.filter(sk -> sk.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + ServiceKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
