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

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_location", schema = "customer")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class ServiceLocation extends Location{
	@Column(name="access_method")
	private String accessMethod;

	@Column(name="site_access_problem")
	private String siteAccessProblem;

	@Column(name="needs_inspection")
	private boolean needsInspection;

	@ElementCollection
	@CollectionTable(name = "usage_points", schema = "customer", joinColumns = {@JoinColumn(name = "usage_point_uuid", nullable = false)})
	@Column(name = "usage_point", nullable = false)
	private Set<String> usagePoints = new HashSet<>();

	@Column(name="outage_block")
	private String outageBlock;

// TODO: Add OneToMany reference for EndDevice

// TODO: Add OneToMany reference for Meter
}
