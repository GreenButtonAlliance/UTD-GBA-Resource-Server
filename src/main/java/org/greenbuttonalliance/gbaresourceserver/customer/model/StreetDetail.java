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

package org.greenbuttonalliance.gbaresourceserver.customer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetDetail {

	@Column(name = "street_number")
	private String number;

	@Column(name = "street_name")
	private String name;

	@Column(name = "street_suffix")
	private String suffix;

	@Column(name = "street_prefix")
	private String prefix;

	@Column(name = "street_type")
	private String type;

	@Column(name = "street_code")
	private String code;

	@Column(name = "street_building_name")
	private String buildingName;

	@Column(name = "street_suite_number")
	private String suiteNumber;

	@Column(name = "street_address_general")
	private String addressGeneral;

	@Column(name = "street_address_general2")
	private String addressGeneral2;

	@Column(name = "street_address_general3")
	private String addressGeneral3;

	@Column(name = "street_within_town_limits")
	private Boolean withinTownLimits;
}
