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
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "interval_block", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class IntervalBlock extends IdentifiedObject {

	@Embedded
	private DateTimeInterval interval;

	@OneToMany(mappedBy = "block", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<IntervalReading> intervalReadings = new HashSet<>();

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "meter_reading_uuid", nullable = false)
	private MeterReading meterReading;

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
}
