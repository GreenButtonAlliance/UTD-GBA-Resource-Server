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

import lombok.AllArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.common.model.IdentifiedObject;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 */
@AllArgsConstructor
public class IdentifiedObjectDtoSubclassFactory<Dto extends IdentifiedObjectDto> {
	private Supplier<Dto> dtoSupplier;

	public <Model extends IdentifiedObject> Dto create(Model base) {
		Dto dto = dtoSupplier.get();
		Optional.ofNullable(base)
			.map(b -> dto.setUuid(b.getUuid())
				.setDescription(b.getDescription())
				.setPublished(b.getPublished())
				.setSelfLinkHref(b.getSelfLinkHref())
				.setUpLinkHref(b.getUpLinkHref())
				.setUpdated(b.getUpdated()));
		return dto;
	}
}
