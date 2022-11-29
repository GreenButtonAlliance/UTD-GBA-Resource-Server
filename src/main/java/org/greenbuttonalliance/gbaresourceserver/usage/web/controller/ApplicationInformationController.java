package org.greenbuttonalliance.gbaresourceserver.usage.web.controller;

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

import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.service.ApplicationInformationService;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.ApplicationInformationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping(path = "/espi/1_1/resource/ApplicationInformation", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationController {
	private final ApplicationInformationService applicationInformationService;

	@GetMapping
	public List<ApplicationInformationDto> getAll() {
		return applicationInformationService.findAll().stream()
			.map(ApplicationInformationDto::fromApplicationInformation)
			.collect(Collectors.toList());
	}

	@GetMapping("/{uuid}")
	public ApplicationInformationDto getByUuid(@PathVariable UUID uuid) {
		ApplicationInformation applicationInformation = ApplicationInformationService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(ApplicationInformation.class, uuid));
		return ApplicationInformationDto.fromApplicationInformation(applicationInformation);
	}
}
