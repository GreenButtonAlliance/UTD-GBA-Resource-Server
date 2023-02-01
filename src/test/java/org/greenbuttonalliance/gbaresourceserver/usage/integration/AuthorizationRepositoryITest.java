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

package org.greenbuttonalliance.gbaresourceserver.usage.integration;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Subscription;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.OAuthError;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ResponseType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenEndpointMethod;
import org.greenbuttonalliance.gbaresourceserver.usage.repository.AuthorizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationRepositoryITest {
	private final AuthorizationRepository authorizationRepository;

	private static final String PRESENT_SELF_LINK = "https://{domain}/DataCustodian/espi/1_1/resource/Authorization/1";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final String DUMMY_STRING = "test1";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		authorizationRepository.deleteAllInBatch();
		authorizationRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
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

	@Test
	public void entityMappings_areNotNull() {
		Authorization fullyMappedAuthorization = authorizationRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedAuthorization != null);

		Function<Authorization, Optional<Subscription>> authorizationSubscriptionInfo = auth -> Optional.ofNullable(auth.getSubscription());

		Assertions.assertAll(
			"Entity mapping failures for customer account " + fullyMappedAuthorization.getUuid(),
			Stream.of(authorizationSubscriptionInfo)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedAuthorization).isPresent()))
		);
	}

	private static List<Authorization> buildTestData() {
		List<Authorization> authorizations = Arrays.asList(
			Authorization.builder()
				.uuid(UUID.randomUUID())
				.description("Green Button Alliance Data Custodian Authorization")
				.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.accessToken(DUMMY_STRING)
				.authorizationUri(null)
				.apDuration(BigInteger.valueOf(21))
				.apStart(BigInteger.valueOf(654))
				.code(DUMMY_STRING)
				.error(OAuthError.INVALID_CLIENT)
				.errorDescription(DUMMY_STRING)
				.errorUri(DUMMY_STRING)
				.expiresIn(BigInteger.valueOf(32164))
				.grantType(1)
				.ppDuration(BigInteger.valueOf(23123))
				.ppStart(BigInteger.valueOf(3241654))
				.refreshToken("23123124646")
				.resourceUri(DUMMY_STRING)
				.responseType(24)
				.scope(DUMMY_STRING)
				.state(DUMMY_STRING)
				.thirdParty(DUMMY_STRING)
				.tokenType(54)
				.applicationInformationId(UUID.randomUUID())
				.retailCustomerId(UUID.randomUUID())
				.subscription(Subscription.builder().selfLinkHref("asdfasdf").build())
				.build()
		);

		// hydrate UUIDs and entity mappings
		authorizations.forEach(auth -> {
			auth.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, auth.getSelfLinkHref()));

			Subscription sub = auth.getSubscription();
			sub.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, auth.getSelfLinkHref()));
			sub.setAuthorization(auth);

			ApplicationInformation ai = ApplicationInformation.builder()
				.description(DUMMY_STRING)
				.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation/2")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
				.upLinkRel("up")
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
				.clientUri(null)
				.contacts(new HashSet<>(Arrays.asList(
					"Contact1",
					"Contact3"
				)))
				.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
				.dataCustodianBulkRequestUri(DUMMY_STRING)
				.dataCustodianId(DUMMY_STRING)
				.dataCustodianResourceEndpoint(DUMMY_STRING)
				.thirdPartyScopeSelectionScreenUri(null)
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
				.thirdPartyScopeSelectionScreenUri(null)
				.dataCustodianScopeSelectionScreenUri(null)
				.grantTypes(new HashSet<>(Arrays.asList(
					GrantType.AUTHORIZATION_CODE
				)))
				.scopes(new HashSet<>(Arrays.asList(
					"Scope4"
				)))
				.build();
			ai.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, auth.getSelfLinkHref()));
			sub.setApplicationInformation(ai);

			sub.setRetailCustomer(RetailCustomer.builder().build());
			sub.getRetailCustomer().setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			// TODO hydrate UsagePoint reference once entity is available
		});
		return authorizations;
	}
}
