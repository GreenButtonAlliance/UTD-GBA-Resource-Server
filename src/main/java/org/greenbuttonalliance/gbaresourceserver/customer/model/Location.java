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

package org.greenbuttonalliance.gbaresourceserver.customer.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.IdentifiedObject;
@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public abstract class Location extends IdentifiedObject {
	@Column
	private String type;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name= "main_address")
	private StreetAddress mainAddress;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name= "secondary_address")
	private StreetAddress secondaryAddress;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="phone1")
	private TelephoneNumber phone1;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="phone2")
	private TelephoneNumber phone2;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="electronic_address")
	private ElectronicAddress electronicAddress;
	@Column(name="geo_info_reference")
	private String geoInfoReference;
	@Column
	private String direction;
	@Column
	private String status;
	//position point
	@Embedded
	private PositionPoint positionPoint;
}
