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
package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "time_configuration", schema = "usage")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class TimeConfiguration extends IdentifiedObject{
	//note: using the correct naming scheme for columns matters, auto generated code and queries assume you follow the correct scheme
	@Column(name= "dst_end_rule")
	private byte[] dstEndRule;
	@Column(name = "dst_offset")
	private Long dstOffset;
	@Column(name = "dst_start_rule")
	private byte[] dstStartRule;
	@Column(name = "tz_offset")
	private Long tzOffset;
	@OneToMany(mappedBy = "timeConfiguration", cascade = CascadeType.ALL)
	private Set<UsagePoint> usagePoints = new HashSet<>();
}
