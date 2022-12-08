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
@Table(name = "retail_customer", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class RetailCustomer extends IdentifiedObject {

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "retail_customer_id", nullable = false)
	private Set<Subscription> subscription = new HashSet<>();

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
}
