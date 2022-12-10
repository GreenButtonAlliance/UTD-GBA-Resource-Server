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

package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.usage.model.*;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;
import org.greenbuttonalliance.gbaresourceserver.usage.service.ApplicationInformationService;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionRepositoryTest {

	private final SubscriptionRepository subscriptionRepository;

	// for testing findById
	private static final String upLinkHref = "https://{domain}/espi/1_1/resource/Subscription";
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/Subscription/174";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		subscriptionRepository.deleteAllInBatch();
		// TODO finish buildTestData();
		subscriptionRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = subscriptionRepository.findById(presentUuid).map(Subscription::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<Subscription> subscription = subscriptionRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			subscription.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, subscription.map(Subscription::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = subscriptionRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		Subscription fullyMappedSubscription = subscriptionRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedSubscription != null);

		Function<Subscription, Optional<ApplicationInformation>> subscriptionToApplicationInformation = sub -> Optional.ofNullable(sub.getApplicationInformation());
		Function<Subscription, Optional<Authorization>> subscriptionToAuthorization = sub -> Optional.ofNullable(sub.getAuthorization());
		Function<Subscription, Optional<RetailCustomer>> subscriptionToRetailCustomer = sub -> Optional.ofNullable(sub.getRetailCustomer());

		Assertions.assertAll(
			"Entity mapping failures for subscription " + fullyMappedSubscription.getUuid(),
			Stream.of(subscriptionToApplicationInformation,
					subscriptionToAuthorization,
					subscriptionToRetailCustomer)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedSubscription).isPresent()))
		);
	}

	private static List<Subscription> buildTestData() {
		List<Subscription> subscriptions = Arrays.asList(
			Subscription.builder()
					.description("description")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref(upLinkHref)
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.hashedId("hashedId")
					.lastUpdate(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.applicationInformation(ApplicationInformation.builder()
						.description("Green Button Alliance Data Custodian Admin Application")
						.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.selfLinkHref(PRESENT_SELF_LINK)
						.selfLinkRel("self")
						.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
						.upLinkRel("up")
						.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.authorizationServerAuthorizationEndpoint("authorizationServerAuthorizationEndpoint")
						.authorizationServerRegistrationEndpoint(null)
						.authorizationServerTokenEndpoint("authorizationServerTokenEndpoint")
						.authorizationServerUri(null)
						.clientId("clientId")
						.clientIdIssuedAt(1403190000L)
						.clientName("clientName")
						.clientSecret("clientSecret")
						.clientSecretExpiresAt(0L)
						.clientUri(null)
						.contacts(new HashSet<>(Arrays.asList(
							"Contact1",
							"Contact2"
						)))
						.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
						.dataCustodianBulkRequestUri("dataCustodianBulkRequestUri")
						.dataCustodianId("dataCustodianId")
						.dataCustodianResourceEndpoint("dataCustodianResourceEndpoint")
						.thirdPartyScopeSelectionScreenUri(null)
						.thirdPartyUserPortalScreenUri(null)
						.logoUri(null)
						.policyUri(null)
						.thirdPartyApplicationDescription(null)
						.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.PRODUCTION)
						.thirdPartyApplicationType(ThirdPartyApplicationType.DEVICE)
						.thirdPartyApplicationUse(ThirdPartyApplicationUse.COMPARISONS)
						.thirdPartyPhone(null)
						.thirdPartyNotifyUri("thirdPartyNotifyUri")
						.redirectUris(new HashSet<>(Arrays.asList(
							"Redirect1"
						)))
						.tosUri(null)
						.softwareId("softwareId")
						.softwareVersion("softwareVersion")
						.tokenEndpointAuthMethod(TokenEndpointMethod.BASIC)
						.responseType(ResponseType.CODE)
						.registrationClientUri("registrationClientUri")
						.registrationAccessToken("registrationAccessToken")
						.thirdPartyScopeSelectionScreenUri(null)
						.dataCustodianScopeSelectionScreenUri(null)
						.grantTypes(new HashSet<>(Arrays.asList(
							GrantType.AUTHORIZATION_CODE
						)))
						.scopes(new HashSet<>(Arrays.asList(
							"Scope1",
							"Scope2",
							"Scope3"
						)))
						.build())
					.authorization(
						Authorization.builder()
							.uuid(UUID.randomUUID())
							.description("Green Button Alliance Data Custodian Authorization")
							.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.selfLinkRel("self")
							.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
							.upLinkRel("up")
							.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
							.accessToken("accessToken")
							.authorizationUri(null)
							.apDuration(BigInteger.valueOf(21))
							.apStart(BigInteger.valueOf(654))
							.code("code")
							.error(OAuthError.INVALID_CLIENT)
							.errorDescription("errorDescription")
							.errorUri("errorUri")
							.expiresIn(BigInteger.valueOf(32164))
							.grantType(1)
							.ppDuration(BigInteger.valueOf(23123))
							.ppStart(BigInteger.valueOf(3241654))
							.refreshToken("23123124646")
							.resourceUri("resourceUri")
							.responseType(24)
							.scope("scope")
							.state("state")
							.thirdParty("thirdParty")
							.tokenType(54)
							.applicationInformationId(UUID.randomUUID())
							.retailCustomerId(UUID.randomUUID())
							.subscription(Subscription.builder().build())
							.build()
					)
					.retailCustomer(RetailCustomer.builder()
						.description("Type of Meter Reading Data")
						.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
						.selfLinkHref(PRESENT_SELF_LINK)
						.selfLinkRel("self")
						.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
						.upLinkRel("up")
						.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
						.enabled(Boolean.TRUE)
						.firstName("hello")
						.lastName("last")
						.password("password")
						.role("whatever")
						.username("Username")
						.build())
//					.usagePointId(1)
					.build(),

			Subscription.builder()
				.description("description")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/Subscription/175")
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.hashedId("hashedId")
				.lastUpdate(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.description("Green Button Alliance Data Custodian Admin Application")
					.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.authorizationServerAuthorizationEndpoint("authorizationServerAuthorizationEndpoint")
					.authorizationServerRegistrationEndpoint(null)
					.authorizationServerTokenEndpoint("authorizationServerTokenEndpoint")
					.authorizationServerUri(null)
					.clientId("clientId")
					.clientIdIssuedAt(1403190000L)
					.clientName("clientName")
					.clientSecret("clientSecret")
					.clientSecretExpiresAt(0L)
					.clientUri(null)
					.contacts(new HashSet<>(Arrays.asList(
						"Contact1",
						"Contact2"
					)))
					.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
					.dataCustodianBulkRequestUri("dataCustodianBulkRequestUri")
					.dataCustodianId("dataCustodianId")
					.dataCustodianResourceEndpoint("dataCustodianResourceEndpoint")
					.thirdPartyScopeSelectionScreenUri(null)
					.thirdPartyUserPortalScreenUri(null)
					.logoUri(null)
					.policyUri(null)
					.thirdPartyApplicationDescription(null)
					.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.PRODUCTION)
					.thirdPartyApplicationType(ThirdPartyApplicationType.DEVICE)
					.thirdPartyApplicationUse(ThirdPartyApplicationUse.COMPARISONS)
					.thirdPartyPhone(null)
					.thirdPartyNotifyUri("thirdPartyNotifyUri")
					.redirectUris(new HashSet<>(Arrays.asList(
						"Redirect1"
					)))
					.tosUri(null)
					.softwareId("softwareId")
					.softwareVersion("softwareVersion")
					.tokenEndpointAuthMethod(TokenEndpointMethod.BASIC)
					.responseType(ResponseType.CODE)
					.registrationClientUri("registrationClientUri")
					.registrationAccessToken("registrationAccessToken")
					.thirdPartyScopeSelectionScreenUri(null)
					.dataCustodianScopeSelectionScreenUri(null)
					.grantTypes(new HashSet<>(Arrays.asList(
						GrantType.AUTHORIZATION_CODE
					)))
					.scopes(new HashSet<>(Arrays.asList(
						"Scope1",
						"Scope2",
						"Scope3"
					)))
					.build())
				.authorization(
					Authorization.builder()
						.uuid(UUID.randomUUID())
						.description("Green Button Alliance Data Custodian Authorization")
						.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.selfLinkHref(PRESENT_SELF_LINK)
						.selfLinkRel("self")
						.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
						.upLinkRel("up")
						.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.accessToken("accessToken")
						.authorizationUri(null)
						.apDuration(BigInteger.valueOf(21))
						.apStart(BigInteger.valueOf(654))
						.code("code")
						.error(OAuthError.INVALID_CLIENT)
						.errorDescription("errorDescription")
						.errorUri("errorUri")
						.expiresIn(BigInteger.valueOf(32164))
						.grantType(1)
						.ppDuration(BigInteger.valueOf(23123))
						.ppStart(BigInteger.valueOf(3241654))
						.refreshToken("23123124646")
						.resourceUri("resourceUri")
						.responseType(24)
						.scope("scope")
						.state("state")
						.thirdParty("thirdParty")
						.tokenType(54)
						.applicationInformationId(UUID.randomUUID())
						.retailCustomerId(UUID.randomUUID())
						.subscription(Subscription.builder().build())
						.build()
				)
				.retailCustomer(RetailCustomer.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.enabled(Boolean.TRUE)
					.firstName("hello")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("Username")
					.build())
//				.usagePointId(1)
				.build(),

			Subscription.builder()
				.description("description")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/Subscription/176")
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.hashedId("hashedId")
				.lastUpdate(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.description("Green Button Alliance Data Custodian Admin Application")
					.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.authorizationServerAuthorizationEndpoint("authorizationServerAuthorizationEndpoint")
					.authorizationServerRegistrationEndpoint(null)
					.authorizationServerTokenEndpoint("authorizationServerTokenEndpoint")
					.authorizationServerUri(null)
					.clientId("clientId")
					.clientIdIssuedAt(1403190000L)
					.clientName("clientName")
					.clientSecret("clientSecret")
					.clientSecretExpiresAt(0L)
					.clientUri(null)
					.contacts(new HashSet<>(Arrays.asList(
						"Contact1",
						"Contact2"
					)))
					.dataCustodianApplicationStatus(DataCustodianApplicationStatus.ON_HOLD)
					.dataCustodianBulkRequestUri("dataCustodianBulkRequestUri")
					.dataCustodianId("dataCustodianId")
					.dataCustodianResourceEndpoint("dataCustodianResourceEndpoint")
					.thirdPartyScopeSelectionScreenUri(null)
					.thirdPartyUserPortalScreenUri(null)
					.logoUri(null)
					.policyUri(null)
					.thirdPartyApplicationDescription(null)
					.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.PRODUCTION)
					.thirdPartyApplicationType(ThirdPartyApplicationType.DEVICE)
					.thirdPartyApplicationUse(ThirdPartyApplicationUse.COMPARISONS)
					.thirdPartyPhone(null)
					.thirdPartyNotifyUri("thirdPartyNotifyUri")
					.redirectUris(new HashSet<>(Arrays.asList(
						"Redirect1"
					)))
					.tosUri(null)
					.softwareId("softwareId")
					.softwareVersion("softwareVersion")
					.tokenEndpointAuthMethod(TokenEndpointMethod.BASIC)
					.responseType(ResponseType.CODE)
					.registrationClientUri("registrationClientUri")
					.registrationAccessToken("registrationAccessToken")
					.thirdPartyScopeSelectionScreenUri(null)
					.dataCustodianScopeSelectionScreenUri(null)
					.grantTypes(new HashSet<>(Arrays.asList(
						GrantType.AUTHORIZATION_CODE
					)))
					.scopes(new HashSet<>(Arrays.asList(
						"Scope1",
						"Scope2",
						"Scope3"
					)))
					.build())
				.authorization(
					Authorization.builder()
						.uuid(UUID.randomUUID())
						.description("Green Button Alliance Data Custodian Authorization")
						.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.selfLinkHref(PRESENT_SELF_LINK)
						.selfLinkRel("self")
						.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
						.upLinkRel("up")
						.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.accessToken("accessToken")
						.authorizationUri(null)
						.apDuration(BigInteger.valueOf(21))
						.apStart(BigInteger.valueOf(654))
						.code("code")
						.error(OAuthError.INVALID_CLIENT)
						.errorDescription("errorDescription")
						.errorUri("errorUri")
						.expiresIn(BigInteger.valueOf(32164))
						.grantType(1)
						.ppDuration(BigInteger.valueOf(23123))
						.ppStart(BigInteger.valueOf(3241654))
						.refreshToken("23123124646")
						.resourceUri("resourceUri")
						.responseType(24)
						.scope("scope")
						.state("state")
						.thirdParty("thirdParty")
						.tokenType(54)
						.applicationInformationId(UUID.randomUUID())
						.retailCustomerId(UUID.randomUUID())
						.subscription(Subscription.builder().build())
						.build()
				)
				.retailCustomer(RetailCustomer.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.enabled(Boolean.TRUE)
					.firstName("hello")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("Username")
					.build())
//				.usagePointId(1)
				.build()
		);

		// hydrate UUIDs and entity mappings

		subscriptions.forEach(sub -> {
			sub.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));

			ApplicationInformation ai =  sub.getApplicationInformation();
			ai.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			ai.setSubscriptions(new HashSet<>(List.of(sub)));

			Authorization auth = sub.getAuthorization();
			auth.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			auth.setSubscription(sub);

			RetailCustomer rc = sub.getRetailCustomer();
			rc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			rc.setSubscriptions(new HashSet<>(List.of(sub)));

			// TODO hydrate connected entities reference when available
		});
		return subscriptions;
	}

}
