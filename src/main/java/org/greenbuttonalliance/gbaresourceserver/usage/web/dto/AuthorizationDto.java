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

package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.flywaydb.core.internal.parser.TokenType;
import org.greenbuttonalliance.gbaresourceserver.common.web.dto.DateTimeIntervalDto;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AuthorizationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.OAuthError;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class AuthorizationDto extends IdentifiedObjectDto implements Serializable {
	//TODO add XML Annotations to all data elements
	private DateTimeIntervalDto authorizedPeriod;
	private DateTimeIntervalDto publishedPeriod;
	private AuthorizationStatus status;
	private Long expires_at;
	private GrantType grant_type;
	private String scope;
	private TokenType token_type;
	private OAuthError error;
	private String error_description;
	private String error_uri;
	private String resourceURI;
	private String authorizationURI;
	private Long customerResourceURI;

	//need to fix dtos
	public static AuthorizationDto fromAuthorization(Authorization authorization) {
		return Optional.ofNullable(authorization)
			.map(au -> new IdentifiedObjectDtoSubclassFactory<>(AuthorizationDto::new).create(au)
					//.setAuthorizedPeriod(DateTimeIntervalDto.fromDateTimeInterval(au.getAuthorizedPeriod()))
					//.setPublishedPeriod(au.getPublishedPeriod())
					//.setStatus(au.getStatus())
					.setExpires_at(au.getExpiresIn().longValue())
					.setGrant_type(GrantType.values()[au.getGrantType()])
					.setScope(au.getScope())
					.setToken_type(TokenType.values()[au.getTokenType()])
					.setError(au.getError())
					.setError_description(au.getErrorDescription())
					.setError_uri(au.getErrorUri())
					.setResourceURI(au.getResourceUri())
					.setAuthorizationURI(au.getAuthorizationUri())
					//.setCustomerResourceURI(au.getCustomerResourceUri())
			)
			.orElse(null);
	}

}
