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

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_location", schema = "customer")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
//technically extends work location, but work location contains nothing and just extends location
public class ServiceLocation extends Location{
	@Column(name = "access_method")
	String accessMethod;
	@Column(name = "site_accesss_problem")
	String siteAccessProblem;
	@Column(name = "needs_inspection")
	boolean needsInspection;
	//made a collection to handle unbounded max occurs
	@ElementCollection
	@CollectionTable(name = "service_location_usage_points", schema = "customer", joinColumns = {@JoinColumn(name = "service_location_uuid", nullable = false)})
	@Column(name = "usage_points", nullable = false)
	private Set<String> usagePoints = new HashSet<>();
	@Column(name = "outage_block")
	private String outageBlock;
}
