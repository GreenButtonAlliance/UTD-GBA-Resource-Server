package org.greenbuttonalliance.gbaresourceserver.usage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;

@Getter
@Setter
@Accessors(chain = true)
public class TimeConfigurationDto extends IdentifiedObjectDto {
	private byte[] dstEndRule;
	private Long dstOffset;
	private byte[] dstStartRule;
	private Long tzOffset;
	public static TimeConfigurationDto fromTimeConfiguration(TimeConfiguration timeConfiguration) {
			return new IdentifiedObjectDtoSubclassFactory<>(TimeConfigurationDto::new).create(timeConfiguration)
				.setDstEndRule(timeConfiguration.getDstEndRule())
				.setDstOffset(timeConfiguration.getDstOffset())
				.setDstStartRule(timeConfiguration.getDstStartRule())
				.setTzOffset(timeConfiguration.getTzOffset());
	}
}
