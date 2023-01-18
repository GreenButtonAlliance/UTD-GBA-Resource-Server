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
import lombok.extern.slf4j.Slf4j;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.service.ApplicationInformationService;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.IdentifiedObjectDto;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.ApplicationInformationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@Transactional
@Slf4j
@RequestMapping(path = "/espi/1_1/resource/ApplicationInformation", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationController {
	private final ApplicationInformationService applicationInformationService;

	@GetMapping
	public String getAll() {
		List<ApplicationInformationDto> listApplicationInformationDto = applicationInformationService.findAll().stream()
			.map(ApplicationInformationDto::fromApplicationInformation).toList();

		StringBuilder contentSB = new StringBuilder();

		for(ApplicationInformationDto applicationInformationDto : listApplicationInformationDto) {
			String content = applicationInformationDto.getContent();
			contentSB.append(applicationInformationDto.addEntryWrapper("ApplicationInformation", content));
		}
		return IdentifiedObjectDto.addParentWrapper(contentSB.toString(), "ApplicationInformation");
	}

	@GetMapping("/{uuid}")
	public String getByUuid(@PathVariable UUID uuid) {

		ApplicationInformation applicationInformation = applicationInformationService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(ApplicationInformation.class, uuid));
		ApplicationInformationDto singleApplicationInformationDto = ApplicationInformationDto.fromApplicationInformation(applicationInformation);

		String content = singleApplicationInformationDto.getContent();

		return singleApplicationInformationDto.addEntryWrapper("ApplicationInformation", content);
	}
}
