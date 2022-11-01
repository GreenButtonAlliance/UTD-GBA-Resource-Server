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

package org.greenbuttonalliance.gbaresourceserver.common.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;

import java.io.Serializable;
import java.util.Optional;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 */
@Getter
@Setter
@Accessors(chain = true)
public class DateTimeIntervalDto implements Serializable {
	private Long start; // in epoch-seconds
	private Long duration; // in seconds

	public static DateTimeIntervalDto fromDateTimeInterval(DateTimeInterval dateTimeInterval) {
		return Optional.ofNullable(dateTimeInterval)
			.map(dti -> new DateTimeIntervalDto()
				.setStart(dti.getStart())
				.setDuration(dti.getDuration()))
			.orElse(null);
	}
}
