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

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.hibernate.annotations.ColumnTransformer;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer_agreement", schema = "customer")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class CustomerAgreement extends Agreement {

	@Column(name = "load_mgmt")
	private String loadMgmt;

	@Column(name = "is_pre_pay")
	private Boolean isPrePay;

	@Column(name = "shut_off_date_time")
	private Long shutOffDateTime; // in epoch-seconds

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_agreement_id", nullable = false)
	private Set<DemandResponseProgram> demandResponsePrograms = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "customer_agreement_pricing_structure", schema = "customer", joinColumns = {@JoinColumn(name = "customer_agreement_uuid", nullable = false)})
	@Column(name = "pricing_structure", nullable = false)
	private Set<String> pricingStructures = new HashSet<>();

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS public.currency)", read = "currency::TEXT")
	private Currency currency;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "customer_agreement_future_status", schema = "customer",
	    joinColumns = {@JoinColumn(name = "customer_agreement_uuid", nullable = false)},
	    inverseJoinColumns = {@JoinColumn(name = "status_id", nullable = false)})
    private Set<Status> futureStatuses = new HashSet<>();

	@Column(name = "agreement_id")
	private String agreementId;
}
