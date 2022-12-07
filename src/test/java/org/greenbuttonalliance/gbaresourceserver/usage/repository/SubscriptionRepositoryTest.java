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
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173";
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

		/* Test bidirectional mappings all the way from IntervalBlock <--> Application Information (IntervalReading) <--> Application Information Scopes (ReadingQuality) here since IntervalReading and ReadingQuality don't have
		their own repositories for which we're testing their individual mappings */
		Function<Subscription, Optional<ApplicationInformation>> subscriptionToApplicationInformation = mr -> Optional.ofNullable(mr.getApplicationInformation());
		Function<Subscription, Optional<RetailCustomer>> subscriptionToRetailCustomer = mr -> Optional.ofNullable(mr.getRetail_customer());

		// TODO test connection from Subscription -> ApplicationInformation -> ApplicationInformationScopes
		// TODO test connection from Subscription -> UsagePoint
		// TODO test for the reverse from ApplicationInformationScopes -> ApplicationInformation -> Subscription

		Assertions.assertAll(
			"Entity mapping failures for block " + fullyMappedSubscription.getUuid(),
			Stream.of(subscriptionToApplicationInformation, subscriptionToRetailCustomer)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedSubscription).isPresent()))
		);
	}

	// TODO buildTestData()
	// TODO List<Subscription> subscription = Arrays.asList(...)
	// TODO ApplicationInformation testApplication = ApplicationInformation.builder()...
	// TODO hydrate UUIDs and entity mappings subscription.forEach(ib -> { ...

	private static List<MeterReading> buildTestData() {
		List<Subscription> subscription;
		DateTimeInterval NullPointerException = null;
		ApplicationInformation test = null;
		RetailCustomer testRetailCustomer = null;

		subscription = Arrays.asList(
			Subscription.builder()
				.description("Fifteen Minute Electricity Consumption")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.authorizationServerAuthorizationEndpoint("testing: authorizationServerAuthorizationEndpoint")
					.authorizationServerRegistrationEndpoint("testing: authorizationServerRegistrationEndpoint")
					.authorizationServerTokenEndpoint("testing: authorizationServerTokenEndpoint")
					.authorizationServerUri("testing: authorizationServerUri")
					.clientId("testing: clientId")
					.clientIdIssuedAt(9223372036854775807L)
					.clientName("testing: clientName")
					.clientSecret("testing: clientSecret")
					.clientSecretExpiresAt(9223372036854775807L)
					.clientUri("testing: clientUri")
					.contacts(Collections.singleton("testing: contacts"))
					.dataCustodianApplicationStatus(DataCustodianApplicationStatus.valueOf("testing: dataCustodianApplicationStatus"))
					.dataCustodianBulkRequestUri("testing: dataCustodianBulkRequestUri")
					.dataCustodianId("testing: dataCustodianId")
					.dataCustodianResourceEndpoint("testing: dataCustodianResourceEndpoint")
					/* deprecated .thirdPartyScopeSelectionScreenUri */
					.thirdPartyUserPortalScreenUri("testing: dataCustodianthirdPartyUserPortalScreenUri")
					.logoUri("testing: logoUri")
					.policyUri("testing: policyUri")
					.thirdPartyApplicationDescription("testing: thirdPartyApplicationDescription")
					.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.valueOf("testing: thirdPartyApplicationStatus"))
					.thirdPartyApplicationType(ThirdPartyApplicationType.valueOf("testing: thirdPartyApplicationType"))
					.thirdPartyApplicationUse(ThirdPartyApplicationUse.valueOf("testing: thirdPartyApplicationUse"))
					.thirdPartyPhone("testing: thirdPartyPhone")
					.thirdPartyNotifyUri("testing: thirdPartyNotifyUri")
					.redirectUris(Collections.singleton("testing: redirectUris"))
					.tosUri("testing: tosUri")
					.softwareId("testing: softwareId")
					.softwareVersion("testing: softwareVersion")
					.tokenEndpointAuthMethod(TokenEndpointMethod.valueOf("testing: tokenEndpointAuthMethod"))
					/* is there a reason why this is a string and not an int? */
					.responseType(ResponseType.valueOf("testing: responseType"))
					.registrationClientUri("testing: registrationClientUri")
					.registrationAccessToken("testing: registrationAccessToken")
					/* deprecated .dataCustodianScopeSelectionScreenUri */
					.grantTypes(new HashSet<>(Arrays.asList(
						GrantType.AUTHORIZATION_CODE
					)))
					.scopes(new HashSet<>(Arrays.asList(
						"Scope4"
					)))
					.build())
				.retail_customer(RetailCustomer.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
					.upLinkRel("up")
					.enabled(Boolean.TRUE)
					.firstName("testing: firstName")
					.lastName("testing: lastName")
					.password("testing: password")
					.role("testing: role")
					.username("testing: username")
					.build())
				.build(),
			Subscription.builder()
				.description("Hourly Wh Received")
				.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading/1")
				.selfLinkRel("self")
				.upLinkHref("DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.applicationInformation(ApplicationInformation.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.authorizationServerAuthorizationEndpoint("testing: authorizationServerAuthorizationEndpoint")
					.authorizationServerRegistrationEndpoint("testing: authorizationServerRegistrationEndpoint")
					.authorizationServerTokenEndpoint("testing: authorizationServerTokenEndpoint")
					.authorizationServerUri("testing: authorizationServerUri")
					.clientId("testing: clientId")
					.clientIdIssuedAt(9223372036854775807L)
					.clientName("testing: clientName")
					.clientSecret("testing: clientSecret")
					.clientSecretExpiresAt(9223372036854775807L)
					.clientUri("testing: clientUri")
					.contacts(Collections.singleton("testing: contacts"))
					.dataCustodianApplicationStatus(DataCustodianApplicationStatus.valueOf("testing: dataCustodianApplicationStatus"))
					.dataCustodianBulkRequestUri("testing: dataCustodianBulkRequestUri")
					.dataCustodianId("testing: dataCustodianId")
					.dataCustodianResourceEndpoint("testing: dataCustodianResourceEndpoint")
					/* deprecated .thirdPartyScopeSelectionScreenUri */
					.thirdPartyUserPortalScreenUri("testing: dataCustodianthirdPartyUserPortalScreenUri")
					.logoUri("testing: logoUri")
					.policyUri("testing: policyUri")
					.thirdPartyApplicationDescription("testing: thirdPartyApplicationDescription")
					.thirdPartyApplicationStatus(ThirdPartyApplicationStatus.valueOf("testing: thirdPartyApplicationStatus"))
					.thirdPartyApplicationType(ThirdPartyApplicationType.valueOf("testing: thirdPartyApplicationType"))
					.thirdPartyApplicationUse(ThirdPartyApplicationUse.valueOf("testing: thirdPartyApplicationUse"))
					.thirdPartyPhone("testing: thirdPartyPhone")
					.thirdPartyNotifyUri("testing: thirdPartyNotifyUri")
					.redirectUris(Collections.singleton("testing: redirectUris"))
					.tosUri("testing: tosUri")
					.softwareId("testing: softwareId")
					.softwareVersion("testing: softwareVersion")
					.tokenEndpointAuthMethod(TokenEndpointMethod.valueOf("testing: tokenEndpointAuthMethod"))
					/* is there a reason why this is a string and not an int? */
					.responseType(ResponseType.valueOf("testing: responseType"))
					.registrationClientUri("testing: registrationClientUri")
					.registrationAccessToken("testing: registrationAccessToken")
					/* deprecated .dataCustodianScopeSelectionScreenUri */
					.grantTypes(new HashSet<>(Arrays.asList(
						GrantType.AUTHORIZATION_CODE
					)))
					.scopes(new HashSet<>(Arrays.asList(
						"Scope4"
					)))
					.build())
				.retail_customer(RetailCustomer.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
					.upLinkRel("up")
					.enabled(Boolean.TRUE)
					.firstName("testing: firstName")
					.lastName("testing: lastName")
					.password("testing: password")
					.role("testing: role")
					.username("testing: username")
					.build())
				.build(),
			Subscription.builder()
				.description("Hourly Wh Delivered")
				.published(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading/2")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.hashedId("testing: hashedId")
				.lastUpdate(NullPointerException)
				.applicationInformation(test)
				.authorization_id(9223372036854775807L)
				.retail_customer(testRetailCustomer)
				.usagepoint_id(12345)
				.build()
		);

		// hydrate UUIDs and entity mappings
		subscription.forEach(mr -> {
			mr.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, mr.getSelfLinkHref()));

			// TODO mr.getUsagePoint().forEach(ib -> { ...

			Optional.ofNullable(mr.getApplicationInformation()).ifPresent(rt -> {
				rt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rt.getSelfLinkHref()));
				rt.setSubscription(mr); // this might be failing bc there isn't a OneToOne relationship in ApplicationInformation.java
			});

			// TODO hydrate UsagePoint reference once entity is available
		});
		return subscription;
	}

}
