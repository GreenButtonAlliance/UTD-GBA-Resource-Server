package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.flywaydb.core.internal.parser.TokenType;
import org.greenbuttonalliance.gbaresourceserver.common.web.dto.DateTimeIntervalDto;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;
import java.time.LocalDateTime;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class AuthorizationDto extends IdentifiedObjectDto implements Serializable {
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

	//need to fix mapping
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
