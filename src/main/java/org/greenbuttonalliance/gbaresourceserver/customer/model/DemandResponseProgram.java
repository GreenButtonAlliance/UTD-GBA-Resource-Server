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

package org.greenbuttonalliance.gbaresourceserver.customer.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.ColumnTransformers;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "demand_response_program", schema = "customer")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class DemandResponseProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "program_name")
	private String programName;

	@Column(name = "enrollment_status")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS public.enrollment_status)", read = "enrollment_status::TEXT")
	private EnrollmentStatus enrollmentStatus;

	@Column(name = "program_description")
	private String programDescription;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "demand_response_program_id", nullable = false)
	private Set<ProgramDate> programDates = new HashSet<>();

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "powerOfTenMultiplier", column = @Column(name = "capacity_reservation_level_potm")),
		@AttributeOverride(name = "timeStamp", column = @Column(name = "capacity_reservation_level_time_stamp")),
		@AttributeOverride(name = "uom", column = @Column(name = "capacity_reservation_level_uom")),
		@AttributeOverride(name = "value", column = @Column(name = "capacity_reservation_level_value")),
		@AttributeOverride(name = "readingTypeRef", column = @Column(name = "capacity_reservation_level_reading_type_ref"))
	})
	@ColumnTransformers({
		@ColumnTransformer(write = "CAST(? AS public.unit_multiplier_kind)", read = "capacity_reservation_level_potm::TEXT"),
		@ColumnTransformer(write = "CAST(? AS public.unit_symbol_kind)", read = "capacity_reservation_level_uom::TEXT")
	})
	private SummaryMeasurement capacityReservationLevel;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "powerOfTenMultiplier", column = @Column(name = "dr_program_nomination_potm")),
		@AttributeOverride(name = "timeStamp", column = @Column(name = "dr_program_nomination_level_time_stamp")),
		@AttributeOverride(name = "uom", column = @Column(name = "dr_program_nomination_level_uom")),
		@AttributeOverride(name = "value", column = @Column(name = "dr_program_nomination_level_value")),
		@AttributeOverride(name = "readingTypeRef", column = @Column(name = "dr_program_nomination_level_reading_type_ref"))
	})
	@ColumnTransformers({
		@ColumnTransformer(write = "CAST(? AS public.unit_multiplier_kind)", read = "dr_program_nomination_potm::TEXT"),
		@ColumnTransformer(write = "CAST(? AS public.unit_symbol_kind)", read = "dr_program_nomination_level_uom::TEXT")
	})
	private SummaryMeasurement drProgramNomination;
}
