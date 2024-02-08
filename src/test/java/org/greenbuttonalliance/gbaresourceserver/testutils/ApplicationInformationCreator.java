/*
 * Copyright (c) 2024 Green Button Alliance, Inc.
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

package org.greenbuttonalliance.gbaresourceserver.testutils;

import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ResponseType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenEndpointMethod;

import java.util.Set;
import java.util.UUID;

/**
 * @author Donald F. Coffin, Green Button Alliance, Inc.
 */
public class ApplicationInformationCreator {
	public static ApplicationInformation create(UUID uuid,
												String description,
												String selfLinkHref,
												String upLinkHref,
												String dataCustodianId,
												DataCustodianApplicationStatus dataCustodianApplicationStatus,
												String thirdPartyApplicationDescription,
												ThirdPartyApplicationStatus thirdPartyApplicationStatus,
												ThirdPartyApplicationType thirdPartyApplicationType,
												ThirdPartyApplicationUse thirdPartyApplicationUse,
												String thirdPartyPhone,
												String authorizationServerUri,
												String thirdPartyNotifyUri,
												String authorizationServerAuthorizationEndpoint,
												String authorizationServerRegistrationEndpoint,
												String authorizationServerTokenEndpoint,
												String dataCustodianBulkRequestUri,
												String dataCustodianResourceEndpoint,
												String thirdPartyScopeSelectionScreenUri,  /* DEPRECATED */
												String thirdPartyUserPortalScreenUri,
												String clientSecret,
												String logoUri,
												String clientName,
												String clientUri,
												Set<String> redirectUris,
												String clientId,
												String tosUri,
												String policyUri,
												String softwareId,
												String softwareVersion,
												long clientIdIssuedAt,
												long clientSecretExpiresAt,
												Set<String> contacts,
												TokenEndpointMethod tokenEndpointAuthMethod,
												Set<String> scopes,
												Set<GrantType> grantTypes,
												ResponseType responseType,
												String registrationClientUri,
												String registrationAccessToken,
												String dataCustodianScopeSelectionScreenUri)
												{
		return ApplicationInformation.builder()
			.uuid(uuid)
			.description("Application Information Record")
			.selfLinkHref(selfLinkHref)
			.upLinkHref(upLinkHref)
			.dataCustodianId(dataCustodianId)
			.dataCustodianApplicationStatus(dataCustodianApplicationStatus)
			.thirdPartyApplicationDescription(thirdPartyApplicationDescription)
			.thirdPartyApplicationStatus(thirdPartyApplicationStatus)
			.thirdPartyApplicationType(thirdPartyApplicationType)
			.thirdPartyApplicationUse(thirdPartyApplicationUse)
			.thirdPartyPhone(thirdPartyPhone)
			.authorizationServerUri(authorizationServerUri)
			.thirdPartyNotifyUri(thirdPartyNotifyUri)
			.authorizationServerAuthorizationEndpoint(authorizationServerAuthorizationEndpoint)
			.authorizationServerRegistrationEndpoint(authorizationServerRegistrationEndpoint)
			.authorizationServerTokenEndpoint(authorizationServerTokenEndpoint)
			.dataCustodianBulkRequestUri(dataCustodianBulkRequestUri)
			.dataCustodianResourceEndpoint(dataCustodianResourceEndpoint)
			.thirdPartyScopeSelectionScreenUri(thirdPartyScopeSelectionScreenUri)
			.clientSecret(clientSecret)
			.logoUri(logoUri)
			.clientName(clientName)
			.clientUri(clientUri)
			.redirectUris(redirectUris)
			.clientId(clientId)
			.tosUri(tosUri)
			.policyUri(policyUri)
			.softwareId(softwareId)
			.softwareVersion(softwareVersion)
			.clientIdIssuedAt(clientIdIssuedAt)
			.clientSecretExpiresAt(clientSecretExpiresAt)
			.contacts(contacts)
			.tokenEndpointAuthMethod(tokenEndpointAuthMethod)
			.scopes(scopes)
			.grantTypes(grantTypes)
			.responseType(responseType)
			.registrationClientUri(registrationClientUri)
			.registrationAccessToken(registrationAccessToken)
			.dataCustodianScopeSelectionScreenUri(dataCustodianScopeSelectionScreenUri)
			.build();
	}
}
