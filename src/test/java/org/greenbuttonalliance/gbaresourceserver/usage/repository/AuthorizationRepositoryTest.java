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

package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Subscription;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AuthorizationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ResponseType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenEndpointMethod;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenType;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationRepositoryTest {
	private final AuthorizationRepository authorizationRepository;

	private static final String AUTHORIZATION_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Authorization/123456";
	private static final String AUTHORIZATION_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Authorization";

	private static final String RETAILCUSTOMER_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/RetailCustomer/654321";
	private static final String RETAILCUSTOMER_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/RetailCustomer";

	private static final String APPLICATIONINFORMATION_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/234567";
	private static final String APPLICATIONINFORMATION_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation";

	private static final String SUBSCRIPTION_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Subscription/234651";
	private static final String SUBSCRIPTION_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Subscription";

	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final String DUMMY_STRING = "test1";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		authorizationRepository.deleteAllInBatch();
		authorizationRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AUTHORIZATION_SELF_LINK);
		UUID foundUuid = authorizationRepository.findById(presentUuid).map(Authorization::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<Authorization> authorization = authorizationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			authorization.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, authorization.map(Authorization::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = authorizationRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	//TODO: Fix code in order to compile successfully
//	@Test
//	public void entityMappings_areNotNull() {
//
//		// Extract fetching authorization logic into a method
//		Authorization fullyMappedAuthorization = authorizationRepository.
//			findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AUTHORIZATION_SELF_LINK)).orElse(null);
//		Assumptions.assumeTrue(fullyMappedAuthorization != null);
//
//		Assertions.assertAll(
//				STR."Entity mapping failures for customer account \{fullyMappedAuthorization.getUuid()}",
//			Stream.of((Function<Authorization, Optional<Subscription>>) auth -> {
//						return Optional.ofNullable(auth.getSubscription());
//					})
//				.map(mappingFunc -> () -> Assertions.assertTrue(mappingFunc.apply(fullyMappedAuthorization).isPresent()))
//		);
//	}

	@Test
	public void entityMappings_areNotNull_Copilot_Test() {
		// Fetch the first Authorization object from the test data
		Authorization fullyMappedAuthorization = authorizationRepository.findAll().get(0);

		// Assert that the Subscription object in the Authorization object is not null
		Assertions.assertNotNull(fullyMappedAuthorization.getSubscription(),
			"Subscription in Authorization is null");
	}

	private static List<Authorization> buildTestData() {
		RetailCustomer rc = RetailCustomer.builder()
			.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, RETAILCUSTOMER_SELF_LINK))
			.description("Retail Customer Description")
			.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
			.selfLinkHref(RETAILCUSTOMER_SELF_LINK)
			.upLinkHref(RETAILCUSTOMER_UP_LINK)
			.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
			.enabled(Boolean.TRUE)
			.firstName("John")
			.lastName("Doe")
			.password("password")
			.role("ROLE_USER")
			.username("jdoe")
			.build();


		List<Authorization> authorizations = Collections.singletonList(
			Authorization.builder()
				.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AUTHORIZATION_SELF_LINK))
				.description("Green Button Alliance Data Custodian Authorization")
				.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref(AUTHORIZATION_SELF_LINK)
				.upLinkHref(AUTHORIZATION_UP_LINK)
				.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.accessToken(DUMMY_STRING)
				.authorizedPeriod(new DateTimeInterval()
					.setDuration(21L)
					.setStart(654L))
				.publishedPeriod(new DateTimeInterval()
					.setDuration(23123L)
					.setStart(3241654L))
				.status(AuthorizationStatus.ACTIVE)
				.expiresAt(32164L)
				.scope(DUMMY_STRING)
				.tokenType(TokenType.BEARER)
				.resourceUri("resourceUri")
				.authorizationUri("authorizationUri")
				.customerResourceUri("customerResourceUri")
				.build()
		);

		// hydrate UUIDs and entity mappings
		authorizations.forEach(auth -> {

			Subscription sub = auth.getSubscription();
			sub.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, auth.getSelfLinkHref()));
			sub.setAuthorization(auth);

			ApplicationInformation ai = ApplicationInformation.builder()
				.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, APPLICATIONINFORMATION_SELF_LINK))
				.description(DUMMY_STRING)
				.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref(APPLICATIONINFORMATION_SELF_LINK)
				.upLinkHref(APPLICATIONINFORMATION_UP_LINK)
				.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.authorizationServerAuthorizationEndpoint(DUMMY_STRING)
				.authorizationServerRegistrationEndpoint(null)
				.authorizationServerTokenEndpoint(DUMMY_STRING)
				.authorizationServerUri(null)
				.clientId(DUMMY_STRING)
				.clientIdIssuedAt(1403190000L)
				.clientName(DUMMY_STRING)
				.clientSecret(DUMMY_STRING)
				.clientSecretExpiresAt(0L)
				.clientUri(DUMMY_STRING)
				.contacts(new HashSet<>(Arrays.asList(
					"Contact1",
					"Contact3"
				)))
				.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
				.dataCustodianBulkRequestUri(DUMMY_STRING)
				.dataCustodianId(DUMMY_STRING)
				.dataCustodianResourceEndpoint(DUMMY_STRING)
				.thirdPartyUserPortalScreenUri(null)
				.logoUri(null)
				.policyUri(null)
				.thirdPartyApplicationDescription(null)
				.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.PRODUCTION)
				.thirdPartyApplicationType(ThirdPartyApplicationType.DEVICE)
				.thirdPartyApplicationUse(ThirdPartyApplicationUse.COMPARISONS)
				.thirdPartyPhone(null)
				.thirdPartyNotifyUri(DUMMY_STRING)
				.redirectUris(new HashSet<>(Arrays.asList(
					"Redirect2",
					"Redirect3"
				)))
				.tosUri(null)
				.softwareId(DUMMY_STRING)
				.softwareVersion(DUMMY_STRING)
				.tokenEndpointAuthMethod(TokenEndpointMethod.BASIC)
				.responseType(ResponseType.CODE)
				.registrationClientUri(DUMMY_STRING)
				.registrationAccessToken(DUMMY_STRING)
				.grantTypes(new HashSet<>(List.of(
						GrantType.AUTHORIZATION_CODE
				)))
				.scopes(new HashSet<>(List.of(
						"Scope4"
				)))
				.build();
			auth.setApplicationInformation(ai);
//			sub.setRetailCustomer(RetailCustomer.builder().build());
//			sub.getRetailCustomer().setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			// TODO hydrate UsagePoint reference once entity is available
		});
		return authorizations;
	}
}
