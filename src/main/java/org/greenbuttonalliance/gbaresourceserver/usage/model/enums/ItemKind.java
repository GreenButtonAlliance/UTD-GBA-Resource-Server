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

public enum ItemKind {
	ENERGY_GENERATION_FEE(1),
	ENERGY_DELIVERY_FEE(2),
	ENERGY_USAGE_FEE(3),
	ADMINISTRATIVE_FEE(4),
	TAX(5),
	ENERGY_GENERATION_CREDIT(6),
	ENERGY_DELIVERY_CREDIT(7),
	ADMINISTRATIVE_CREDIT(8),
	PAYMENT(9),
	INFORMATION(10);

	public final int schemaValue;
	ItemKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static ItemKind getItemKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(ItemKind.class).stream()
			.filter(ik -> ik.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + ItemKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
