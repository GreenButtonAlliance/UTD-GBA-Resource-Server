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

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.IdentifiedObject;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "retail_customer", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class RetailCustomer extends IdentifiedObject {

	@Column
	private Boolean enabled;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column
	private String password;

	@Column
	private String role;

	@Column
	private String username;

	@OneToMany(mappedBy = "retailCustomer", cascade = CascadeType.ALL)
	private Set<UsagePoint> usagePoints = new HashSet<>();

//	@OneToMany(mappedBy = "retailCustomer", cascade = CascadeType.ALL)
//	private Set<ApplicationInformation> applicationInformations = new HashSet<>();

	@OneToMany(mappedBy = "retailCustomer", cascade = CascadeType.ALL)
	private Set<Subscription> subscriptions = new HashSet<>();

}
