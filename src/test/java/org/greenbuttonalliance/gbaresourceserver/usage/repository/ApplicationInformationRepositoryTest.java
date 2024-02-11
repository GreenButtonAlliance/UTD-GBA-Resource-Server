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

package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.testutils.ApplicationInformationCreator;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ResponseType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenEndpointMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationRepositoryTest {
	private final ApplicationInformationRepository applicationInformationRepository;

	private static final String AI_SELF_LINK_HREF = "https://data.greenbuttonconnect.org/DataCustodian/espi/1_1/" +
		"resource/ApplicationInformation/135497";
	private static final String AI_UP_LINK_HREF = "https://data.greenbuttonconnect.org/DataCustodian/espi/1_1/resource/" +
			"ApplicationInformation";
	private static final String AUTH_SERVER_URI = "https://data.greenbuttonconnect.org";
	private static final String AUTH_SERVER_AUTHORIZATION_ENDPOINT =
		"https://data.greenbuttonconnect.org/oauth/authorize";
	private static final String AUTH_SERVER_REGISTRATION_ENDPOINT = "https://data.greenbuttonconnect.org/oauth/register";
	private static final String AUTH_SERVER_TOKEN_ENDPOINT = "https://data.greenbuttonconnect.org/oauth/token";
	private static final String DATA_CUSTODIAN_BATCH_BULK_ENDPOINT = "https://data.greenbuttonconnect.org/" +
		"DataCustodian/espi/1_1/resource/Batch/Bulk";
	private static final String DATA_CUSTODIAN_RESOURCE_ENDPOINT = "https://data.greenbuttonconnect.org/DataCustodian/" +
		"espi/1_1/resource";
	private static final String OAUTH_SCOPE_REDIRECT_URI = "https://data.greenbuttonconnect.org/ThirdParty/espi/1_1/" +
		"OAuthCallBack";
	private static final String THIRD_PARTY_NOTIFY_URI = "https://data.greenbuttonconnect.org/ThirdParty/espi/1_1/" +
		"Notification";
	private static final String THIRD_PARTY_USER_PORTAL_SCREEN_URI = "https://data.greenbuttonconnect.org/ThirdParty";
	private static final String CLIENT_SECRET = "331b4eea-c2e5-45cb-b5f8-631c096895b1";
	private static final String REGISTRATION_ACCESS_TOKEN = "Kgv7tXvwHbg2ahL6CgVuTmHuwbnmibs27jAD9cu-CI0";
	private static final String FOOBAR_SELF_LINK = "foobar";
	private static final String NULL_ENTRY = null;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		applicationInformationRepository.deleteAllInBatch();
		applicationInformationRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AI_SELF_LINK_HREF);
		UUID foundUuid = applicationInformationRepository.findById(presentUuid).map(ApplicationInformation::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, FOOBAR_SELF_LINK);
		Optional<ApplicationInformation> applicationInformation = applicationInformationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			applicationInformation.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, applicationInformation.map(ApplicationInformation::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = applicationInformationRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		ApplicationInformation fullyMappedApplicationInformation =
			applicationInformationRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL,
				AI_SELF_LINK_HREF)).orElse(null);
		Assertions.assertNotNull(fullyMappedApplicationInformation, "fullyMappedApplicationInformation should " +
			"not be null");
	}


	private static List<ApplicationInformation> buildTestData() {

		UUID uuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AI_SELF_LINK_HREF);

		List<ApplicationInformation> applicationInformations = Arrays.asList(
			ApplicationInformationCreator.create(
				uuid,
				"Application Information Record",
				AI_SELF_LINK_HREF,
				AI_UP_LINK_HREF,
				"GBA_DataCustodian_1_1",
				DataCustodianApplicationStatus.PRODUCTION,
				"GBA_Example_Third_Party_Application",
				ThirdPartyApplicationStatus.PRODUCTION,
				ThirdPartyApplicationType.WEB,
				ThirdPartyApplicationUse.ENERGY_MANAGEMENT,
				"(909) 123-4567",
				AUTH_SERVER_URI,
				THIRD_PARTY_NOTIFY_URI,
				AUTH_SERVER_AUTHORIZATION_ENDPOINT,
				AUTH_SERVER_REGISTRATION_ENDPOINT,
				AUTH_SERVER_TOKEN_ENDPOINT,
				DATA_CUSTODIAN_BATCH_BULK_ENDPOINT,
				DATA_CUSTODIAN_RESOURCE_ENDPOINT,
				NULL_ENTRY,
				THIRD_PARTY_USER_PORTAL_SCREEN_URI,
				CLIENT_SECRET,
				NULL_ENTRY,
				"Green Button Alliance, Inc.",
				THIRD_PARTY_USER_PORTAL_SCREEN_URI,	// clientUri and thirdPartyUserPortalScreenUri are the same
				new HashSet<>(List.of(OAUTH_SCOPE_REDIRECT_URI)),
				"GBA_GB_Client",
				NULL_ENTRY,
				NULL_ENTRY,
				"GBA_GB_Client",
				"1.0",
				1706715921L,
				0L,
				new HashSet<>(Arrays.asList("dfcoffin@greenbuttonalliance.org", "greg.l.turnquist@gmail.com")),
				TokenEndpointMethod.BASIC,
				new HashSet<>(Arrays.asList("Scope1", "Scope2", "Scope3")),
				new HashSet<>(List.of(GrantType.AUTHORIZATION_CODE, GrantType.CLIENT_CREDENTIALS,
					GrantType.REFRESH_TOKEN)),
				ResponseType.CODE,
				AUTH_SERVER_REGISTRATION_ENDPOINT,
				REGISTRATION_ACCESS_TOKEN,
				NULL_ENTRY
			)
		);

		return applicationInformations;
	}
}
