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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customer_account", schema = "customer")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class CustomerAccount extends Document {

	@Id
	private UUID uuid;

	@Column(name = "billing_cycle")
	private String billingCycle;

	@Column(name = "budget_bill")
	private String budgetBill;

	@Column(name = "last_bill_amount")
	private Long lastBillAmount;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "contact_info_id")
	private Organisation contactInfo;

	@Column(name = "account_id")
	private String accountId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_account_id", nullable = false)
	private Set<AccountNotification> accountNotifications = new HashSet<>();
}
