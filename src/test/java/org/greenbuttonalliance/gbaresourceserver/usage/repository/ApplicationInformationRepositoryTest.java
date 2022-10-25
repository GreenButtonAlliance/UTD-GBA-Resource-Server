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

package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformationGrantTypes;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformationScope;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationRepositoryTest {
	private final ApplicationInformationRepository applicationInformationRepository;
	private static final String SELF_LINK= "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/183";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final String DUMMY_STRING= "test1";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@BeforeEach
	public void initTestData() {
		applicationInformationRepository.deleteAllInBatch();
		applicationInformationRepository.saveAll(buildTestData());
	}
	public static List<ApplicationInformation> buildTestData()
	{
		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();
		List<ApplicationInformation> applicationInformations = Arrays.asList(ApplicationInformation.builder()
			.published(LocalDateTime.parse("2011-07-03 12:09:38", SQL_FORMATTER))
			.selfLinkHref(SELF_LINK)
			.selfLinkRel("self")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
			.upLinkRel("up")
			.updated(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
			.authorizationServerAuthorizationEndpoint(DUMMY_STRING)
			.authorizationServerRegistrationEndpoint(DUMMY_STRING)
				.authorizationServerTokenEndpoint(DUMMY_STRING)
				.authorizationServerURI(DUMMY_STRING)
				.clientId(DUMMY_STRING)
				.clientIdIssuedAt(354440004L)
				.clientSecretExpiresAt(1234566788990L)
				.clientURI(DUMMY_STRING)
				.contacts(deadbeefs)
				.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
				.dataCustodianBulkRequestURI(DUMMY_STRING)
				.dataCustodianDefaultBatchResource(DUMMY_STRING)
				.dataCustodianDefaultSubscriptionResource(DUMMY_STRING)
				.dataCustodianId(DUMMY_STRING)
				.dataCustodianResourceEndpoint(DUMMY_STRING)
				.dataCustodianThirdPartySelectionScreenURI(DUMMY_STRING)
				.logoURI(DUMMY_STRING)
				.policyURI(DUMMY_STRING)
				.thirdPartyApplicationDescription(DUMMY_STRING)
				.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.LIVE)
				.thirdPartyApplicationType(ThirdPartyApplicationType.DEVICE)
				.thirdPartyApplicationUse(ThirdPartyApplicationUse.COMPARISONS)
				.thirdPartyPhone(DUMMY_STRING)
				.thirdPartyNotifyUri(DUMMY_STRING)
				.thirdPartyUserPortalScreen(DUMMY_STRING)
				.redirectURI(DUMMY_STRING)
				.tosURI(DUMMY_STRING)
				.softwareID(DUMMY_STRING)
				.softwareVersion(DUMMY_STRING)
				.tokenEndpointAuthMethod(TokenEndPointMethod.BASIC)
				.responseTypes(ResponseTypes.CODE)
				.registrationClientURI(DUMMY_STRING)
				.registrationAccessToken(DUMMY_STRING)
				.grantTypes(Stream.of(
					new ApplicationInformationGrantTypes()
						.setGrantTypes(GrantTypes.AUTHORIZATION_CODE),
					new ApplicationInformationGrantTypes()
						.setGrantTypes(GrantTypes.CLIENT_CREDENTIALS)
				).collect(Collectors.toSet()))
				.applicationScope(Stream.of(
					new ApplicationInformationScope()
						.setScope("testestest"),
					new ApplicationInformationScope()
						.setScope("testestest2")
				).collect(Collectors.toSet()))
			.build(),
			ApplicationInformation.builder()


				.build()
			);
	}
}
