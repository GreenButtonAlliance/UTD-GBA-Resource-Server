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

package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ReadingType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class ReadingTypeDto extends IdentifiedObjectDto implements Serializable {
	private AccumulationKind accumulationBehavior;
	private CommodityKind commodity;
	private Short consumptionTier;
	private Currency currency;
	private DataQualifierKind dataQualifier;
	private QualityOfReading defaultQuality;
	private FlowDirectionKind flowDirection;
	private Long intervalLength; //Java doesn't support unsigned integers, so just using Long
	private MeasurementKind kind;
	private PhaseCodeKind phase;
	private UnitMultiplierKind powerOfTenMultiplier;
	private TimePeriodOfInterest timeAttribute;
	private Short tou;
	private UnitSymbolKind uom;
	private Short cpp;
//	private ReadingInterHarmonic interharmonic;
	private TimeAttributeKind measuringPeriod;
//	private RationalNumber argument;

	public static ReadingTypeDto fromReadingType(ReadingType readingType) {
		return Optional.ofNullable(readingType)
			.map(rt -> new IdentifiedObjectDtoSubclassFactory<>(ReadingTypeDto::new).create(rt)
				.setAccumulationBehavior(rt.getAccumulationBehavior())
				.setCommodity(rt.getCommodity())
				.setConsumptionTier(rt.getConsumptionTier())
				.setCurrency(rt.getCurrency())
				.setDataQualifier(rt.getDataQualifier())
				.setDefaultQuality(rt.getDefaultQuality())
				.setFlowDirection(rt.getFlowDirection())
				.setIntervalLength(rt.getIntervalLength())
				.setKind(rt.getKind())
				.setPhase(rt.getPhase())
				.setPowerOfTenMultiplier(rt.getPowerOfTenMultiplier())
//				.setTimeAttribute(rt.getTimeAttribute())
				.setTou(rt.getTou())
				.setUom(rt.getUom())
				.setCpp(rt.getCpp())
//				.setInterharmonic(rt.getInterharmonic())
				.setMeasuringPeriod(rt.getMeasuringPeriod()))
//				.setArgument(rt.getArgument())
			.orElse(null);
	}
}
