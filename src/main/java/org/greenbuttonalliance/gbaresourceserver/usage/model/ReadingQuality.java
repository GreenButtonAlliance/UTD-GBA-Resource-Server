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
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.QualityOfReading;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "reading_quality", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
public class ReadingQuality {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.quality_of_reading)", read = "quality::TEXT")
	private QualityOfReading quality;

	@ManyToOne(optional = false)
	@JoinColumn(name = "reading_id", nullable = false)
	private IntervalReading reading;

	/**
	 * This implementation only checks if the encapsulated {@link QualityOfReading} fields are equal and does not check the IDs. This is useful when used with {@link java.util.Set}, as it makes sense
	 * any <code>Set</code> to only contain unique <code>QualityOfReading</code>s. If users of this class need a "stronger" version of equality, they can manually check for equivalent IDs.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}

		ReadingQuality rqObj = (ReadingQuality)obj;
		return this.quality == rqObj.quality;
	}
}
