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

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ItemKind;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "line_item", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {

	@Id
	private UUID uuid;

	@Column
	@NonNull
	private Long amount;

	@Column
	private Long rounding;

	@Column(name = "date_time")
	@NonNull
	private LocalDateTime dateTime;

	@Column
	private String note;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "powerOfTenMultiplier", column = @Column(name = "power_of_ten_multiplier")),
		@AttributeOverride(name = "timeStamp", column = @Column(name = "time_stamp")),
		@AttributeOverride(name = "readingTypeRef", column = @Column(name = "reading_type_ref"))
	})
	private SummaryMeasurement measurement;

	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.item_kind)", read = "item_kind::TEXT")
	@Column(name = "item_kind")
	private ItemKind itemKind;

	@Column(name = "unit_cost")
	private Long unitCost;

	@Embedded
	private DateTimeInterval itemPeriod;

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "usage_summary_uuid", nullable = false)
	private UsageSummary usageSummary;
}
