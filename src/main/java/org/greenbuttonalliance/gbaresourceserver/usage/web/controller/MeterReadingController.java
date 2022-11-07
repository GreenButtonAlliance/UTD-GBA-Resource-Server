package org.greenbuttonalliance.gbaresourceserver.usage.web.controller;

import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.MeterReading;
import org.greenbuttonalliance.gbaresourceserver.usage.service.MeterReadingService;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.MeterReadingDto;
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
@RequestMapping(path = "/espi/1_1/resource/MeterReading", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadingController {
	private final MeterReadingService MeterReadingService;

	@GetMapping
	public List<MeterReadingDto> getAll() {
		return MeterReadingService.findAll().stream()
			.map(MeterReadingDto::fromMeterReading)
			.collect(Collectors.toList());
	}

	@GetMapping("/{uuid}")
	public MeterReadingDto getByUuid(@PathVariable UUID uuid) {
		MeterReading MeterReading = MeterReadingService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(MeterReading.class, uuid));
		return MeterReadingDto.fromMeterReading(MeterReading);
	}
}
