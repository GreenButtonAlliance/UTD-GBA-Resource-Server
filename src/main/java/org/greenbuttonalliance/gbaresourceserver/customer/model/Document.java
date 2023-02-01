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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.IdentifiedObject;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public abstract class Document extends IdentifiedObject {

	@Column
	private String type;

	@Column(name = "author_name")
	private String authorName;

	@Column(name = "created_date_time")
	private Long createdDateTime; // in epoch-seconds

	@Column(name = "last_modified_date_time")
	private Long lastModifiedDateTime; // in epoch-seconds

	@Column(name = "revision_number")
	private String revisionNumber;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "electronic_address_id")
	private ElectronicAddress electronicAddress;

	@Column
	private String subject;

	@Column
	private String title;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "doc_status_id")
	private Status docStatus;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "status_id")
	private Status status;

	@Column
	private String comment;
}
