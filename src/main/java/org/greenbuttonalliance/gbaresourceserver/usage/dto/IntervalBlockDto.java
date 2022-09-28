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

package org.greenbuttonalliance.gbaresourceserver.usage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 */
@Getter
@Setter
@Accessors(chain = true)
public class IntervalBlockDto extends IdentifiedObjectDto {
	private DateTimeInterval interval;
	private Set<IntervalReadingDto> IntervalReading = new HashSet<>(); // unusual naming convention to match NAESB schema

	public static IntervalBlockDto fromIntervalBlock(IntervalBlock intervalBlock) {
		/* Calculates the start and duration for a collection of IntervalReadings by taking the earliest start date and calculating the duration from that
		until the latest possible end date */
		Optional<Long> overallStart = intervalBlock.getIntervalReadings().stream()
			.map(ir -> ir.getStart())
			.reduce(BinaryOperator.minBy(Long::compareTo));
		Optional<Integer> overallDuration = overallStart.flatMap(os -> intervalBlock.getIntervalReadings().stream()
			.map(ir -> ir.getStart() + ir.getDuration())
			.reduce(BinaryOperator.maxBy(Long::compareTo))
			.map(overallEnd -> Math.toIntExact(overallEnd - os)));

		return new IdentifiedObjectDtoSubclassFactory<>(IntervalBlockDto::new).create(intervalBlock)
			.setInterval(new DateTimeInterval()
				.setDuration(overallDuration.orElse(null))
				.setStart(overallStart.orElse(null)))
			.setIntervalReading(intervalBlock.getIntervalReadings().stream()
				.map(IntervalReadingDto::fromIntervalReading)
				.collect(Collectors.toSet()));
	}
}
