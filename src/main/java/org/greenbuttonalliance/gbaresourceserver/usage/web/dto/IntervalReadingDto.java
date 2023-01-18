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
import org.greenbuttonalliance.gbaresourceserver.common.web.dto.DateTimeIntervalDto;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalReading;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 */
@Getter
@Setter
@Accessors(chain = true)
public class IntervalReadingDto implements Serializable {
	private Long cost;
	private Set<ReadingQualityDto> ReadingQuality = new HashSet<>(); // unusual naming convention to match NAESB schema
	private DateTimeIntervalDto timePeriod;
	private Long value;
	private Short consumptionTier;
	private Short tou;
	private Short cpp;

	public static IntervalReadingDto fromIntervalReading(IntervalReading intervalReading) {
		return Optional.ofNullable(intervalReading)
			.map(ir -> new IntervalReadingDto()
				.setCost(ir.getCost())
				.setReadingQuality(ir.getReadingQualities().stream()
					.map(ReadingQualityDto::fromReadingQuality)
					.collect(Collectors.toSet()))
				.setTimePeriod(DateTimeIntervalDto.fromDateTimeInterval(ir.getTimePeriod()))
				.setValue(ir.getValue())
				.setConsumptionTier(ir.getConsumptionTier())
				.setTou(ir.getTou())
				.setCpp(ir.getCpp()))
			.orElse(null);
	}
}
