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

package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LifecycleDate {

	@Column(name = "manufactured_date")
	private Long manufacturedDate; // in seconds

	@Column(name = "purchase_date")
	private Long purchaseDate; // in seconds

	@Column(name = "received_date")
	private Long receivedDate; // in seconds

	@Column(name = "installation_date")
	private Long installationDate; // in seconds

	@Column(name = "removal_date")
	private Long removalDate; // in seconds

	@Column(name = "retired_date")
	private Long retiredDate; // in seconds
}
