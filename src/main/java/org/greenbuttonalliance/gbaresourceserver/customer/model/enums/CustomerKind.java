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

package org.greenbuttonalliance.gbaresourceserver.customer.model.enums;

import java.util.EnumSet;

public enum CustomerKind {
	RESIDENTIAL("residential"),
	RESIDENTIAL_AND_COMMERCIAL("residentialAndCommercial"),
	RESIDENTIAL_AND_STREETLIGHT("residentialAndStreetlight"),
	RESIDENTIAL_STREETLIGHT_OTHERS("residentialStreetlightOthers"),
	RESIDENTIAL_FARM_SERVICE("residentialFarmService"),
	COMMERCIAL_INDUSTRIAL("commercialIndustrial"),
	PUMPING_LOAD("pumpingLoad"),
	WIND_MACHINE("windMachine"),
	ENERGY_SERVICE_SUPPLIER("energyServiceSupplier"),
	ENERGY_SERVICE_SCHEDULER("energyServiceScheduler"),
	ENTERPRISE("enterprise"),
	REGIONAL_OPERATOR("regionalOperator"),
	SUBSIDIARY("subsidiary"),
	INTERNAL_USE("internalUse"),
	OTHER("other");

	public final String schemaValue;

	CustomerKind(String schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static CustomerKind getCustomerKindFromSchemaValue(String schemaValue) {
		return EnumSet.allOf(CustomerKind.class).stream()
			.filter(ck -> ck.schemaValue.equals(schemaValue))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + CustomerKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
