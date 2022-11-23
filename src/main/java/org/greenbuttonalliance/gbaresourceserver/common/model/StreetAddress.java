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

package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class StreetAddress {
	//streetDetail, only used here so not splitting off
	@Column
	private String streetNumber;
	@Column
	private String streetName;
	@Column
	private String streetSuffix;
	@Column
	private String streetPrefix;
	@Column
	private String streetType;
	@Column
	private String streetCode;
	@Column
	private String buildingName;
	@Column
	private String suitNumber;
	@Column
	private String addressGeneral;
	@Column
	private String addressGeneral2;
	@Column
	private String addressGeneral3;
	@Column
	private boolean withinTownLimits;
	//town detail
	@Column
	private String townCode;
	@Column
	private String townSection;
	@Column
	private String townName;
	@Column
	private String county;
	@Column
	private String stateOrProvince;
	@Column
	private String country;
	//Status will be split off
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="statusValue",column = @Column(name="status_value")),
		@AttributeOverride(name="dateTime",column = @Column(name="date_time")),
		@AttributeOverride(name="remark",column = @Column(name="remark")),
		@AttributeOverride(name="reason",column = @Column(name="reason"))
	})
	Status streetAddressStatus;
	@Column
	String postalCode;
	@Column
	String poBox;
}
