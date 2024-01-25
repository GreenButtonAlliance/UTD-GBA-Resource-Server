/*
 * Copyright (c) 2022-2024 Green Button Alliance, Inc.
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
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "interval_reading", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
public class IntervalReading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long cost;

	@OneToMany(mappedBy = "reading", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ReadingQuality> readingQualities = new HashSet<>();

	@Embedded
	private DateTimeInterval timePeriod;

	@Column
	private Long value; // in units specified by associated ReadingType

	@ManyToOne(optional = false)
	@JoinColumn(name = "block_uuid", nullable = false)
	private IntervalBlock block;

	@Column(name = "consumption_tier")
	private Short consumptionTier;

	@Column
	private Short tou;

	@Column
	private Short cpp;
}
