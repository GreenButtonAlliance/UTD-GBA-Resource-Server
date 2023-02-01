/*
 * Copyright (c) 2023 Green Button Alliance, Inc.
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

/**
 * @author Donald F. Coffin
 */
public enum TokenType {
	BEARER("Bearer");

	public final String schemaValue;

	TokenType(String schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static TokenType getTokenTypeFromSchemaValue(String schemaValue) {
		return EnumSet.allOf(TokenType.class).stream()
			.filter(gt -> gt.schemaValue.equals(schemaValue))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + TokenType.class.getCanonicalName() +
				" with schemaValue " + schemaValue));
	}
}
