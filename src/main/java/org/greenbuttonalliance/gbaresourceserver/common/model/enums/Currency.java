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

package org.greenbuttonalliance.gbaresourceserver.common.model.enums;

import java.util.EnumSet;

public enum Currency {
	USD(840),
	EUR(978),
	AUD(36),
	CAD(124),
	CHF(756),
	CNY(156),
	DKK(208),
	GBP(826),
	JPY(392),
	NOK(578),
	RUB(643),
	SEK(752),
	INR(356),
	OTHER(0);

	public final int schemaValue;

	Currency(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static Currency getCurrencyFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(Currency.class).stream()
			.filter(c -> c.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + Currency.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
