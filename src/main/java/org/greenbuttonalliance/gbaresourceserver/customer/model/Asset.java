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

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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
import org.greenbuttonalliance.gbaresourceserver.usage.model.IdentifiedObject;


@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public abstract class Asset extends IdentifiedObject {

	@Column
	private String type;

	@Column(name = "utc_number")
	private String utcNumber;

	@Column(name = "serial_number")
	private String serialNumber;

	@Column(name = "lot_number")
	private String lotNumber;

	@Column(name = "purchase_price")
	private Long purchasePrice;

	@Column
	private Boolean critical;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "electronic_address_id")
	private ElectronicAddress electronicAddress;

	@Embedded
	private LifecycleDate lifecycle;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride( name = "type", column = @Column(name = "acceptance_type")),
		@AttributeOverride( name = "success", column = @Column(name = "acceptance_success")),
		@AttributeOverride( name = "dateTime", column = @Column(name = "acceptance_date_time"))
	})
	private AcceptanceTest acceptanceTest;

	@Column(name = "initial_condition")
	private String initialCondition;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride( name = "percent", column = @Column(name = "initial_loss_of_life"))
	})
	private PerCent initialLossOfLife;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "status_id")
	private Status status;
}
