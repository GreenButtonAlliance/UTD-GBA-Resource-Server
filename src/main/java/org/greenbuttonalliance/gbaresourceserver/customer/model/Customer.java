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

package org.greenbuttonalliance.gbaresourceserver.customer.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.customer.model.enums.CustomerKind;
import org.hibernate.annotations.ColumnTransformer;

import java.util.UUID;

@Entity
@Table(name = "customer", schema = "customer")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class Customer extends OrganisationRole {

	@Id
	private UUID uuid;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS customer.customer_kind)", read = "kind::TEXT")
	private CustomerKind kind;

	@Column(name = "special_need")
	private String specialNeed;

	@Column
	private Boolean vip;

	@Column(name = "puc_number")
	private String pucNumber;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "status_id")
	private Status status;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "priority_id")
	private Priority priority;

	@Column
	private String locale;

	@Column(name = "customer_name")
	private String customerName;
}
