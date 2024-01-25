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

package org.greenbuttonalliance.gbaresourceserver.customer.model.enums;

import java.util.EnumSet;

public enum SupplierKind {
	UTILITY("utility"),
	RETAILER("retailer"),
	OTHER("other"),
	LSA("lse"),
	MDMA("mdma"),
	MSP("msp");

	public final String schemaValue;

	SupplierKind(String schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static SupplierKind getSupplierKindFromSchemaValue(String schemaValue) {
		return EnumSet.allOf(SupplierKind.class).stream()
			.filter(sk -> sk.schemaValue.equals(schemaValue))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + SupplierKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
