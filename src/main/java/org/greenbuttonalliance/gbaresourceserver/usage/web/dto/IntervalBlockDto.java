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

package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.common.web.dto.DateTimeIntervalDto;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;

import javax.xml.bind.annotation.XmlRootElement;
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
public class IntervalBlockDto extends IdentifiedObjectDto {
	private DateTimeIntervalDto interval;
	private Set<IntervalReadingDto> IntervalReading = new HashSet<>(); // unusual naming convention to match NAESB schema

	public static IntervalBlockDto fromIntervalBlock(IntervalBlock intervalBlock) {
		return Optional.ofNullable(intervalBlock)
			.map(ib -> new IdentifiedObjectDtoSubclassFactory<>(IntervalBlockDto::new).create(ib)
				.setInterval(DateTimeIntervalDto.fromDateTimeInterval(ib.getInterval()))
				.setIntervalReading(ib.getIntervalReadings().stream()
					.map(IntervalReadingDto::fromIntervalReading)
					.collect(Collectors.toSet())))
			.orElse(null);
	}
}
