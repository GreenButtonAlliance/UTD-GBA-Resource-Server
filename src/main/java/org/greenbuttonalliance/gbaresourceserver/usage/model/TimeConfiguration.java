package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.sql.Blob;

@Entity
@Table(name = "Time_Configuration", schema = "usage")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class TimeConfiguration extends IdentifiedObject{
	//note: using the correct naming scheme for columns matters, auto generated code adn queries assume you follow the correct scheme
	@Column
	private byte[] dstEndRule;
	@Column
	private Long dstOffset;
	@Column
	private byte[] dstStartRule;
	@Column
	private Long tzOffset;
}
