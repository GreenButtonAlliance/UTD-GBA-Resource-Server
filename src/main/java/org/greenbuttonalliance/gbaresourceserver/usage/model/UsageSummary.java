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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.common.model.BillingChargeSource;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.TariffRiderRefs;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.CommodityKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.QualityOfReading;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

@Entity
@Table(name = "usage_summary", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UsageSummary extends IdentifiedObject{

	@Embedded
	private DateTimeInterval billingPeriod;

	@Column(name = "bill_last_period")
	private Long billLastPeriod;

	@Column(name = "bill_to_date")
	private Long billToDate;

	@Column(name = "cost_additional_last_period")
	private Long costAdditionalLastPeriod;

//	@JoinColumn()
//	@OneToMany()
//	private LineItem costAdditionalDetailLastPeriod;

	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.currency)", read = "currency::TEXT")
	@Column
	private Currency currency;

	@Embedded
	private SummaryMeasurement overallConsumptionLastPeriod;

	@Embedded
	private SummaryMeasurement currentBillingPeriodOverAllConsumption;

	@Embedded
	private SummaryMeasurement currentDayLastYearNetConsumption;

	@Embedded
	private SummaryMeasurement currentDayNetConsumption;

	@Embedded
	private SummaryMeasurement currentDayOverallConsumption;

	@Embedded
	private SummaryMeasurement peakDemand;

	@Embedded
	private SummaryMeasurement previousDayLastYearOverallConsumption;

	@Embedded
	private SummaryMeasurement previousDayNetConsumption;

	@Embedded
	private SummaryMeasurement previousDayOverallConsumption;

	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.quality_of_reading)", read = "quality_of_reading::TEXT")
	@Column(name = "quality_of_reading")
	private QualityOfReading qualityOfReading;

	@Embedded
	private SummaryMeasurement ratchetDemand;

	@Embedded
	private DateTimeInterval ratchetDemandPeriod;

	@Column(name = "status_time_stamp")
	private LocalDateTime statusTimeStamp;

	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.commodity_kind)", read = "commodity::TEXT")
	@Column(name = "commodity")
	private CommodityKind commodity;

	@Column(name = "tariff_profile")
	private String tariffProfile;

	@Column(name = "read_cycle")
	private String readCycle;

	@Embedded
	private TariffRiderRefs tariffRiderRefs;

	@Embedded
	private BillingChargeSource billingChargeSource;
}
