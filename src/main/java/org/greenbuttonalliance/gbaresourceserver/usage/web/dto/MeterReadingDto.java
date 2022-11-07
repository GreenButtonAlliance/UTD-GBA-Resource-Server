package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.common.web.dto.DateTimeIntervalDto;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.model.MeterReading;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
public class MeterReadingDto extends IdentifiedObjectDto {
//	private DateTimeIntervalDto interval;
//	private Set<IntervalBlockDto> IntervalReading = new HashSet<>(); // unusual naming convention to match NAESB schema


	public static MeterReadingDto fromMeterReading(MeterReading meterReading) {
		return Optional.ofNullable(meterReading)
			.map(ib -> new IdentifiedObjectDtoSubclassFactory<>(MeterReadingDto::new).create(ib))
//				.setInterval(DateTimeIntervalDto.fromDateTimeInterval(ib.getInterval()))
//				.setIntervalReading(ib.getIntervalReadings().stream()
//					.map(IntervalReadingDto::fromIntervalReading)
//					.collect(Collectors.toSet())))
			.orElse(null);
	}
}
