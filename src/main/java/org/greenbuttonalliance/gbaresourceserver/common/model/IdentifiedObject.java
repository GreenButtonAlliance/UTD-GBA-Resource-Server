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
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public abstract class IdentifiedObject {

	@Id
	private UUID uuid;

	@Column(name = "title")
	private String description;

	@Column(nullable = false)
	private LocalDateTime published;

	@Column(name = "self_link_href", nullable = false)
	private String selfLinkHref;

	@Column(name = "up_link_href", nullable = false)
	private String upLinkHref;

	@Column(nullable = false)
	private LocalDateTime updated;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}

		IdentifiedObject iObj = (IdentifiedObject)obj;
		return Objects.equals(uuid, iObj.uuid);
	}
}
