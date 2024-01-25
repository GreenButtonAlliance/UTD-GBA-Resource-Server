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
import org.junit.jupiter.api.Assumptions;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionRepositoryTest {

	private final SubscriptionRepository subscriptionRepository;

	// for testing findById
	private static final String AUTHORIZATION_1_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Authorization/123456";
	private static final String AUTHORIZATION_2_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Authorization/234561";
	private static final String AUTHORIZATION_3_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Authorization/345612";
	private static final String AUTHORIZATION_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Authorization";

	private static final String RETAILCUSTOMER_1_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/RetailCustomer/654321";
	private static final String RETAILCUSTOMER_2_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/RetailCustomer/543216";
	private static final String RETAILCUSTOMER_3_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/RetailCustomer/432165";
	private static final String RETAILCUSTOMER_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/RetailCustomer";

	private static final String APPLICATIONINFORMATION_1_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/234567";
	private static final String APPLICATIONINFORMATION_2_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/345672";
	private static final String APPLICATIONINFORMATION_3_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/456723";
	private static final String APPLICATIONINFORMATION_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation";

	private static final String SUBSCRIPTION_1_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Subscription/234651";
	private static final String SUBSCRIPTION_2_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Subscription/346512";
	private static final String SUBSCRIPTION_3_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Subscription/465123";
	private static final String SUBSCRIPTION_UP_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/resource/Subscription";

	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		subscriptionRepository.deleteAllInBatch();
		subscriptionRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SUBSCRIPTION_1_SELF_LINK);
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
		String searchUUID = String.valueOf(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SUBSCRIPTION_1_SELF_LINK));
		Subscription fullyMappedSubscription =
			subscriptionRepository.findById(UUID.fromString(searchUUID)).orElse(null);
		Assumptions.assumeTrue(fullyMappedSubscription != null);

		Function<Subscription, Optional<ApplicationInformation>> subscriptionToApplicationInformation = sub -> Optional.ofNullable(sub.getApplicationInformation());
		Function<Subscription, Optional<Authorization>> subscriptionToAuthorization = sub -> Optional.ofNullable(sub.getAuthorization());
		Function<Subscription, Optional<RetailCustomer>> subscriptionToRetailCustomer = sub -> Optional.ofNullable(sub.getRetailCustomer());

		Assertions.assertAll(
			STR."Entity mapping failures for subscription \{fullyMappedSubscription.getUuid()}",
			Stream.of(subscriptionToApplicationInformation,
					subscriptionToAuthorization,
					subscriptionToRetailCustomer)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedSubscription).isPresent()))
		);
	}

	private static List<Subscription> buildTestData() {
		//TODO: Add TimeConfiguration and UsagePoint to Subscription
		List<Subscription> subscriptions = Arrays.asList(
			Subscription.builder()
				.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SUBSCRIPTION_1_SELF_LINK))
				.description("Subscription")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref(SUBSCRIPTION_1_SELF_LINK)
				.upLinkHref(SUBSCRIPTION_UP_LINK)
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.hashedId("hashedId")
				.lastUpdate(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, APPLICATIONINFORMATION_1_SELF_LINK))
					.description("Application Information")
					.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.selfLinkHref(APPLICATIONINFORMATION_1_SELF_LINK)
					.upLinkHref(APPLICATIONINFORMATION_UP_LINK)
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
							.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AUTHORIZATION_1_SELF_LINK))
							.description("Green Button Alliance Data Custodian Authorization")
							.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
							.selfLinkHref(AUTHORIZATION_1_SELF_LINK)
							.upLinkHref(AUTHORIZATION_UP_LINK)
							.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
							.authorizedPeriod(new DateTimeInterval()
								.setDuration(21L)
								.setStart(654L))
							.publishedPeriod(new DateTimeInterval()
								.setDuration(23123L)
								.setStart(3241654L))
							.authorizationUri(null)
							.status(AuthorizationStatus.ACTIVE)
							.expiresAt(32164L)
							.tokenType(TokenType.BEARER)
							.resourceUri("resourceUri")
							.authorizationUri("authorizationUri")
							.customerResourceUri("customerResourceUri")
							.build()
					)
					.retailCustomer(RetailCustomer.builder()
						.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, RETAILCUSTOMER_1_SELF_LINK))
						.description("Retail Customer Data")
						.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
						.selfLinkHref(RETAILCUSTOMER_1_SELF_LINK)
						.upLinkHref(RETAILCUSTOMER_UP_LINK)
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
				.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SUBSCRIPTION_2_SELF_LINK))
				.description("description")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref(SUBSCRIPTION_2_SELF_LINK)
				.upLinkHref(SUBSCRIPTION_UP_LINK)
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.hashedId("hashedId")
				.lastUpdate(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, APPLICATIONINFORMATION_2_SELF_LINK))
					.description("Application Information Data")
					.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.selfLinkHref(APPLICATIONINFORMATION_2_SELF_LINK)
					.upLinkHref(APPLICATIONINFORMATION_UP_LINK)
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
						.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AUTHORIZATION_2_SELF_LINK))
						.description("Green Button Alliance Data Custodian Authorization")
						.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.selfLinkHref(AUTHORIZATION_2_SELF_LINK)
						.upLinkHref(APPLICATIONINFORMATION_UP_LINK)
						.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.authorizedPeriod(new DateTimeInterval()
								.setDuration(21L)
								.setStart(654L))
						.publishedPeriod(new DateTimeInterval()
								.setDuration(23123L)
								.setStart(3241654L))
						.status(AuthorizationStatus.ACTIVE)
						.expiresAt(32164L)
						.scope("scope")
						.tokenType(TokenType.BEARER)
						.resourceUri("resourceUri")
						.authorizationUri(null)
						.customerResourceUri("customerResourceUri")
						.build()
				)
				.retailCustomer(RetailCustomer.builder()
					.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, RETAILCUSTOMER_2_SELF_LINK))
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(RETAILCUSTOMER_2_SELF_LINK)
					.upLinkHref(RETAILCUSTOMER_UP_LINK)
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
				.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SUBSCRIPTION_3_SELF_LINK))
				.description("description")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref(SUBSCRIPTION_3_SELF_LINK)
				.upLinkHref(SUBSCRIPTION_UP_LINK)
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.hashedId("hashedId")
				.lastUpdate(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, APPLICATIONINFORMATION_3_SELF_LINK))
					.description("Application Information Data")
					.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
					.selfLinkHref(APPLICATIONINFORMATION_3_SELF_LINK)
					.upLinkHref(APPLICATIONINFORMATION_UP_LINK)
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
						.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, AUTHORIZATION_3_SELF_LINK))
						.description("Green Button Alliance Data Custodian Authorization")
						.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.selfLinkHref(AUTHORIZATION_3_SELF_LINK)
						.upLinkHref(AUTHORIZATION_UP_LINK)
						.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
						.authorizedPeriod(new DateTimeInterval()
								.setDuration(21L)
								.setStart(654L))
						.publishedPeriod(new DateTimeInterval()
								.setDuration(23123L)
								.setStart(3241654L))
						.status(AuthorizationStatus.ACTIVE)
						.expiresAt(32164L)
						.scope("scope")
						.tokenType(TokenType.BEARER)
						.resourceUri("resourceUri")
						.authorizationUri(null)
						.customerResourceUri("customerResourceUri")
						.build()
				)
				.retailCustomer(RetailCustomer.builder()
					.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, RETAILCUSTOMER_3_SELF_LINK))
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(RETAILCUSTOMER_3_SELF_LINK)
					.upLinkHref(RETAILCUSTOMER_UP_LINK)
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
//			sub.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));

			ApplicationInformation ai =  sub.getApplicationInformation();
//			ai.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			ai.setSubscriptions(new HashSet<>(List.of(sub)));

			Authorization auth = sub.getAuthorization();
//			auth.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			auth.setSubscription(sub);
			auth.setApplicationInformation(ai);
			auth.setRetailCustomer(sub.getRetailCustomer());

			RetailCustomer rc = sub.getRetailCustomer();
//			rc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, sub.getSelfLinkHref()));
			rc.setSubscriptions(new HashSet<>(List.of(sub)));

			// TODO hydrate connected entities reference when available
		});
		return subscriptions;
	}
}
