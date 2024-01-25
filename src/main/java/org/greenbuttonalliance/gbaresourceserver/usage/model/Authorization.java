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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.IdentifiedObject;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AuthorizationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenType;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "authorization", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
//@RequiredArgsConstructor
public class Authorization extends IdentifiedObject {

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "duration", column = @Column(name = "authorized_period_duration")),
		@AttributeOverride(name = "start", column = @Column(name = "authorized_period_start"))
	})
	private DateTimeInterval authorizedPeriod;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "duration", column = @Column(name = "published_period_duration")),
		@AttributeOverride(name = "start", column = @Column(name = "published_period_start"))
	})
	private DateTimeInterval	publishedPeriod;

	@Column(name = "access_token", nullable = false)
	private String accessToken;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(nullable = false)
	private AuthorizationStatus status;

	@Column(name = "expires_at", nullable = false)
	private Long expiresAt;  // in epoch-seconds

//	@Column(name = "grant_type")
//	private GrantType grantType;

	@Column(nullable = false)
	private String scope;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.token_type)", read = "token_type::TEXT")
	private TokenType tokenType;

//	@Column
//	@Enumerated(EnumType.STRING)
//	@ColumnTransformer(write = "CAST(? AS usage.oauth_error)", read = "error::TEXT")
//	private OAuthError error;

//	@Column(name = "error_description")
//	private String errorDescription;

//	@Column(name = "error_uri")
//	private String errorUri;

	@Column(name = "resource_uri", nullable = false)
	private String resourceUri;

	@Column(name = "authorization_uri", nullable = false)
	private String authorizationUri;

	@Column(name = "customer_resource_uri")
	private String customerResourceUri;

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "application_information_id", nullable = false)
	private ApplicationInformation applicationInformation;

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "retail_customer_id", nullable = false)
	private RetailCustomer retailCustomer;

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "subscription_id", nullable = false)
	private Subscription subscription;
}
