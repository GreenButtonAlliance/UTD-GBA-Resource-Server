/*
 *
 *  * Copyright (c) 2022 Green Button Alliance, Inc.
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantTypes;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnTransformer;
import java.util.UUID;

@Entity
@Table(name = "reading_quality", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
public class ApplicationInformationGrantTypes {
	@Id
	private UUID uuid;
	@Column(name="grant_types")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.grant_types)", read = "grant_types::TEXT")
	private GrantTypes grantTypes;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "application_information_uuid")
	private ApplicationInformation applicationInformation;
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}

		ApplicationInformationGrantTypes gtObj = (ApplicationInformationGrantTypes) obj;
		return this.grantTypes == gtObj.grantTypes;
	}
}
