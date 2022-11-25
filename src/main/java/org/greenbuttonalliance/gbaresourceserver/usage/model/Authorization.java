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

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.OAuthError;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IdentifiedObject;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "authorization", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class Authorization extends IdentifiedObject {

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "authorization_uri")
	private String authorizationUri;

	@Column(name = "ap_duration")
	private BigInteger apDuration;

	@Column(name = "ap_start")
	private BigInteger apStart;

	@Column
	private String code;

	@Column
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.o_auth_error)", read = "error::TEXT")
	private OAuthError error;

	@Column(name = "error_description")
	private String errorDescription;

	@Column(name = "error_uri")
	private String errorUri;

	@Column(name = "expires_in")
	private BigInteger expiresIn;

	@Column(name = "grant_type")
	private int grantType;

	@Column(name = "pp_duration")
	private BigInteger ppDuration;

	@Column(name = "pp_start")
	private BigInteger ppStart;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "resource_uri")
	private String resourceUri;

	@Column(name = "response_type")
	private int responseType;

	@Column
	private String scope;

	@Column
	private String state;

	@Column(name = "third_party")
	private String thirdParty;

	@Column(name = "token_type")
	private int tokenType;

	@Column(name = "application_information_id")
	private UUID applicationInformationId;

	@Column(name = "retail_customer_id")
	private UUID retailCustomerId;

	@Column(name = "subscription_id")
	private UUID subscriptionId;
}
