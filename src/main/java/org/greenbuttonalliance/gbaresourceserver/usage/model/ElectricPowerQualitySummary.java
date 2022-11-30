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

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;

@Entity
@Table(name = "electric_power_quality_summaries", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class ElectricPowerQualitySummary extends IdentifiedObject {

	@Column(name = "flicker_plt")
	private Long flickerPlt;

	@Column(name = "flicker_pst")
	private Long flickerPst;

	@Column(name = "harmonic_voltage")
	private Long harmonicVoltage;

	@Column(name = "long_interruptions")
	private Long longInterruptions;

	@Column(name = "mains_voltage")
	private Long mainsVoltage;

	@Column(name = "measurement_protocol")
	private Short measurementProtocol;

	@Column(name = "power_frequency")
	private Long powerFrequency;

	@Column(name = "rapid_voltage_changes")
	private Long rapidVoltageChanges;

	@Column(name = "short_interruptions")
	private Long shortInterruptions;

	@Embedded
	private DateTimeInterval summaryInterval;

	@Column(name = "supply_voltage_dips")
	private Long supplyVoltageDips;

	@Column(name = "supply_voltage_imbalance")
	private Long supplyVoltageImbalance;

	@Column(name = "supply_voltage_variations")
	private Long supplyVoltageVariations;

	@Column(name = "temp_overvoltage")
	private Long tempOvervoltage;

	@Override
	public String getDataCustodianBulkRequestURI() {
		return null;
	}

	@Override
	public String getThirdPartyScopeSelectionURI() {
		return null;
	}

	@Override
	public String getThirdPartyUserPortalScreenURI() {
		return null;
	}

	@Override
	public String getClient_secret() {
		return null;
	}

	@Override
	public String getLogo_uri() {
		return null;
	}

	@Override
	public String getClient_name() {
		return null;
	}

	@Override
	public String getClient_uri() {
		return null;
	}

	@Override
	public String getRedirect_uri() {
		return null;
	}

	@Override
	public String getClient_id() {
		return null;
	}

	@Override
	public String getTos_uri() {
		return null;
	}

	@Override
	public String getPolicy_uri() {
		return null;
	}

	@Override
	public String getSoftware_id() {
		return null;
	}

	//TODO: Add this once UsagePoint entity is created
//	@ManyToOne(optional = false)
//	@JoinColumn(name = "usage_point_uuid", nullable = false)
//	private UsagePoint usagePoint;
}
