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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AccumulationKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.CommodityKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataQualifierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.FlowDirectionKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.MeasurementKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.PhaseCodeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.QualityOfReading;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TimeAttributeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitSymbolKind;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "reading_type", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class ReadingType extends IdentifiedObject {

	@Column(name = "accumulation_behavior")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.accumulation_kind)", read = "accumulation_behavior::TEXT")
	private AccumulationKind accumulationBehavior;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.commodity_kind)", read = "commodity::TEXT")
	private CommodityKind commodity;

	@Column(name = "consumption_tier")
	private Short consumptionTier;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.currency)", read = "currency::TEXT")
	private Currency currency;

	@Column(name = "data_qualifier")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.data_qualifier_kind)", read = "data_qualifier::TEXT")
	private DataQualifierKind dataQualifier;

	@Column(name = "default_quality")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.quality_of_reading)", read = "default_quality::TEXT")
	private QualityOfReading defaultQuality;

	@Column(name = "flow_direction")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.flow_direction_kind)", read = "flow_direction::TEXT")
	private FlowDirectionKind flowDirection;

	@Column(name = "interval_length")
	private Long intervalLength;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.measurement_kind)", read = "kind::TEXT")
	private MeasurementKind kind;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.phase_code_kind)", read = "phase::TEXT")
	private PhaseCodeKind phase;

	@Column(name = "power_of_ten_multiplier")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.unit_multiplier_kind)", read = "power_of_ten_multiplier::TEXT")
	private UnitMultiplierKind powerOfTenMultiplier;

	@Column(name = "time_attribute")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.time_period_of_interest)", read = "time_attribute::TEXT")
	private TimeAttributeKind timeAttribute;

	@Column
	private Short tou;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.unit_symbol_kind)", read = "uom::TEXT")
	private UnitSymbolKind uom;

	@Column
	private Short cpp;

	@Column(name = "interharmonic_numerator")
	private Long interharmonicNumerator;

	@Column(name = "interharmonic_denominator")
	private Long interharmonicDenominator;

	@Column(name = "measuring_period")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.time_attribute_kind)", read = "measuring_period::TEXT")
	private TimeAttributeKind measuringPeriod;

	@Column(name = "argument_numerator")
	private Long argumentNumerator;

	@Column(name = "argument_denominator")
	private Long argumentDenominator;

	// MeterReadings can exist without a corresponding ReadingType, so don't cascade delete operations through CascadeType.ALL
	@OneToOne(mappedBy = "readingType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private MeterReading meterReading;
}
