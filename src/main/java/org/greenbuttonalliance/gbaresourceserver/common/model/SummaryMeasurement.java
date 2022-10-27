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

package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitSymbolKind;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class SummaryMeasurement {

	@Column(name = "power_of_ten_multiplier")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.unit_multiplier_kind)", read = "power_of_ten_multiplier::TEXT")
	private UnitMultiplierKind powerOfTenMultiplier;

	@Column(name = "time_stamp")
	private LocalDateTime timeStamp;

	@Column
	private UnitSymbolKind uom;

	@Column
	private Long value;

	@Column(name = "reading_type_ref")
	private String readingTypeRef;

}
