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

public enum OAuthError {
	INVALID_REQUEST(1),
	INVALID_CLIENT(2),
	INVALID_GRANT(3),
	UNAUTHORIZED_CLIENT(4),
	UNSUPPORTED_GRANT_TYPE(5),
	INVALID_SCOPE(6),
	INVALID_REDIRECT_URI(7),
	INVALID_CLIENT_METADATA(8),
	INVALID_CLIENT_ID(9),
	ACCESS_DENIED(10),
	UNSUPPORTED_RESPONSE_TYPE(11),
	SERVER_ERROR(12),
	TEMPORARILY_UNAVAILABLE(13);

	public final int schemaValue;

	OAuthError(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static OAuthError getOAuthErrorFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(OAuthError.class).stream()
			.filter(qor -> qor.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + OAuthError.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
