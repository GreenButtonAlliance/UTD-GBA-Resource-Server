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
