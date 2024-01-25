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

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "service_delivery_point", schema = "usage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ServiceDeliveryPoint {

	@Id
	private UUID uuid;

	@Column
	private String name;

	@Column(name = "tariff_profile")
	private String tariffProfile;

	@Column(name = "customer_agreement")
	private String customerAgreement;

	@OneToMany(mappedBy = "serviceDeliveryPoint", cascade = CascadeType.ALL)
	private Set<UsagePoint> usagePoints = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "service_delivery_point_tariff_rider_ref", schema = "usage",
		joinColumns = {@JoinColumn(name = "service_delivery_point_uuid", nullable = false)},
		inverseJoinColumns = {@JoinColumn(name = "tariff_rider_ref_id", nullable = false)})
	private Set<TariffRiderRef> tariffRiderRefs = new HashSet<>();
}
