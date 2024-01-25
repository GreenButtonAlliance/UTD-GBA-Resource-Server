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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AnodeType;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "aggregate_node_ref", schema = "usage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregateNodeRef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(name = "anode_type")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.anode_type)", read = "anode_type::TEXT")
	private AnodeType anodeType;

	@NonNull
	@Column
	private String ref;

	@Column(name = "start_effective_date")
	private Long startEffectiveDate; //in epoch-seconds

	@Column(name = "end_effective_date")
	private Long endEffectiveDate; //in epoch-seconds

	//TODO: double check this mapping
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pnode_id")
	private PnodeRef pnodeRef;
}
