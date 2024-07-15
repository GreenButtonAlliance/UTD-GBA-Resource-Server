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

package org.greenbuttonalliance.gbaresourceserver;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ResponseType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenEndpointMethod;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Donald F. Coffin, Green Button Alliance, Inc.
 **/
public class TestDataBuilder {

	// General test data basic URL strings
	private static final String DC_BASE_URL = """
		https://localhost:8080/DataCustodian/espi/1_1/resource""";
	private static final String TP_BASE_URL = """
		https://localhost:8080/ThirdParty/espi/1_1""";

	// ApplicationInformation test data URL strings
	private static final String APPLICATIONINFORMATION_SELF_LINK = STR."\{DC_BASE_URL}/ApplicationInformation";
	private static final String APPLICATIONINFORMATION_UP_LINK = STR."\{DC_BASE_URL}/ApplicationInformation";
	private static final String AUTHORIZATION_ENDPOINT = STR."\{DC_BASE_URL}/Authorization";
	private static final String REGISTRATION_ENDPOINT = STR."\{DC_BASE_URL}/Registration";
	private static final String REGISTRATION_CLIENT_URI = STR."\{DC_BASE_URL}/ApplicationInformation/135497";
	private static final String TOKEN_ENDPOINT = STR."\{DC_BASE_URL}/Token";
	private static final String BULK_REQUEST_URI = STR."\{DC_BASE_URL}/Batch/Bulk/Bulk_1";
	private static final String NOTIFICATION_URI = STR."\{TP_BASE_URL}/Notification";
	private static final String REDIRECT_URI = STR."\{TP_BASE_URL}/OAuthCallBack";


	public static ApplicationInformation buildTestApplicationInformation(
		String selfLinkHref,
		String clientID,
		String dataCustodianID) {
		return ApplicationInformation.builder()
			.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, selfLinkHref))
			.description("Green Button Alliance Data Custodian Admin Application")
			.selfLinkHref(APPLICATIONINFORMATION_SELF_LINK)
			.upLinkHref(APPLICATIONINFORMATION_UP_LINK)
			.authorizationServerAuthorizationEndpoint(AUTHORIZATION_ENDPOINT)
			.authorizationServerRegistrationEndpoint(REGISTRATION_ENDPOINT)
			.authorizationServerTokenEndpoint(TOKEN_ENDPOINT)
			.clientId(clientID)
			.clientIdIssuedAt(1600000000L)
			.clientName("GBA CMD Certification Test Application")
			.clientSecret("secret")
			.clientSecretExpiresAt(0L)
			.contacts(new HashSet<>(Arrays.asList("test.contact1@example.com", "test.contact2@example.com")))
			.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
			.dataCustodianBulkRequestUri(BULK_REQUEST_URI)
			.dataCustodianId(dataCustodianID)
			.dataCustodianResourceEndpoint(DC_BASE_URL)
			.thirdPartyNotifyUri(NOTIFICATION_URI)
			.redirectUris(new HashSet<>(Arrays.asList(REDIRECT_URI)))
			.softwareId("software")
			.softwareVersion("1.0.0")
			.tokenEndpointAuthMethod(TokenEndpointMethod.BASIC)
			.responseType(ResponseType.CODE)
			.registrationClientUri(REGISTRATION_CLIENT_URI)
			.registrationAccessToken("59b4ae6d-8540-5211-b983-d463cde763fb")
			.grantTypes(new HashSet<>(Arrays.asList(
				GrantType.AUTHORIZATION_CODE,
				GrantType.CLIENT_CREDENTIALS,
				GrantType.REFRESH_TOKEN)))
			.scopes(new HashSet<>(Arrays.asList(
				"FB=1_3_4_5_6_7_8_9_13_15_16_17_27_28_31_35_37_39_40_51_53_54_55_56_57_58_59_60_61_62_64_65_67_69_70",
				"SCOPE2", "SCOPE3")))
			.build();
	}
}
