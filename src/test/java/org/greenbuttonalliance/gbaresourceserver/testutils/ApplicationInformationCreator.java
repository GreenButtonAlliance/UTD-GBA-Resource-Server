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

	private static final String AUTH_SERVER_URI = "https://data.greenbuttonconnect.org";
	private static final String AUTH_SERVER_AUTHORIZATION_ENDPOINT =
		"https://data.greenbuttonconnect.org/oauth/authorize";
	private static final String AUTH_SERVER_REGISTRATION_ENDPOINT = "https://data.greenbuttonconnect.org/oauth/register";
	private static final String AUTH_SERVER_TOKEN_ENDPOINT = "https://data.greenbuttonconnect.org/oauth/token";
	private static final String DATA_CUSTODIAN_BATCH_BULK_ENDPOINT = "https://data.greenbuttonconnect.org/" +
		"DataCustodian/espi/1_1/resource/Batch/Bulk";
	private static final String DATA_CUSTODIAN_RESOURCE_ENDPOINT = "https://data.greenbuttonconnect.org/DataCustodian/" +
		"espi/1_1/resource";
	private static final String THIRD_PARTY_NOTIFY_URI = "https://data.greenbuttonconnect.org/ThirdParty/espi/1_1/" +
		"Notification";
	private static final String THIRD_PARTY_USER_PORTAL_SCREEN_URI = "https://data.greenbuttonconnect.org/ThirdParty";
	private static final String REGISTRATION_ACCESS_TOKEN = "Kgv7tXvwHbg2ahL6CgVuTmHuwbnmibs27jAD9cu-CI0";

	public static ApplicationInformation create(
		UUID uuid,
		String selfLinkHref,
		String upLinkHref,
		String clientSecret,
		Set<String> redirectUris,
		Set<String> contacts,
		TokenEndpointMethod tokenEndpointAuthMethod,
		Set<String> scopes,
		Set<GrantType> grantTypes
	)
	{
		return ApplicationInformation.builder()
			.uuid(uuid)
			.description("Application Information Record")
			.selfLinkHref(selfLinkHref)
			.upLinkHref(upLinkHref)
			.dataCustodianId("GBA_DataCustodian_1_1")
			.dataCustodianApplicationStatus(DataCustodianApplicationStatus.PRODUCTION)
			.thirdPartyApplicationDescription("GBA_Example_Third_Party_Application")
			.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.PRODUCTION)
			.thirdPartyApplicationType(ThirdPartyApplicationType.WEB)
			.thirdPartyApplicationUse(ThirdPartyApplicationUse.ENERGY_MANAGEMENT)
			.thirdPartyPhone("(909) 123-4567")
			.authorizationServerUri(AUTH_SERVER_URI)
			.thirdPartyNotifyUri(THIRD_PARTY_NOTIFY_URI)
			.authorizationServerAuthorizationEndpoint(AUTH_SERVER_AUTHORIZATION_ENDPOINT)
			.authorizationServerRegistrationEndpoint(AUTH_SERVER_REGISTRATION_ENDPOINT)
			.authorizationServerTokenEndpoint(AUTH_SERVER_TOKEN_ENDPOINT)
			.dataCustodianBulkRequestUri(DATA_CUSTODIAN_BATCH_BULK_ENDPOINT)
			.dataCustodianResourceEndpoint(DATA_CUSTODIAN_RESOURCE_ENDPOINT)
			.thirdPartyUserPortalScreenUri(THIRD_PARTY_USER_PORTAL_SCREEN_URI)
			.clientSecret(clientSecret)
			.logoUri(null)
			.clientName("Green Button Alliance, Inc.")
			.clientUri(THIRD_PARTY_USER_PORTAL_SCREEN_URI)
			.redirectUris(redirectUris)
			.clientId("GBA_GB_Client")
			.tosUri(null)
			.policyUri(null)
			.softwareId("GBA_GB_Client")
			.softwareVersion("1.0")
			.clientIdIssuedAt(1706715921L)
			.clientSecretExpiresAt(0L)
			.contacts(contacts)
			.tokenEndpointAuthMethod(tokenEndpointAuthMethod)
			.scopes(scopes)
			.grantTypes(grantTypes)
			.responseType(ResponseType.CODE)
			.registrationClientUri(AUTH_SERVER_REGISTRATION_ENDPOINT)
			.registrationAccessToken(REGISTRATION_ACCESS_TOKEN)
			.build();
	}
}
