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
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "tariff_rider_ref", schema = "usage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffRiderRef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rider_type")
	private String riderType;

	@Column(name = "enrollment_status")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS public.enrollment_status)", read = "enrollment_status::TEXT")
	private EnrollmentStatus enrollmentStatus;

	@Column(name = "effective_date")
	private Long effectiveDate; //in epoch-seconds
}
