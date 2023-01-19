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

package org.greenbuttonalliance.gbaresourceserver.usage.web.controller;

import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ReadingType;
import org.greenbuttonalliance.gbaresourceserver.usage.service.ReadingTypeService;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.ReadingTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping(path = "/espi/1_1/resource/ReadingType", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReadingTypeController {
	private final ReadingTypeService ReadingTypeService;

	@GetMapping
	public List<ReadingTypeDto> getAll() {
		return ReadingTypeService.findAll().stream()
			.map(ReadingTypeDto::fromReadingType)
			.collect(Collectors.toList());
	}

	@GetMapping("/{uuid}")
	public ReadingTypeDto getByUuid(@PathVariable UUID uuid) {
		ReadingType ReadingType = ReadingTypeService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(ReadingType.class, uuid));
		return ReadingTypeDto.fromReadingType(ReadingType);
	}
}
