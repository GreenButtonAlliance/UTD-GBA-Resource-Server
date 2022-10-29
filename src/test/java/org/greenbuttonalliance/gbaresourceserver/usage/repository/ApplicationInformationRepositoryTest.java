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

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;

import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformationGrantTypes;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformationScope;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//reading quality isn't even part of the imports, where is it coming from
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationRepositoryTest {
	private final ApplicationInformationRepository applicationInformationRepository;
	private static final String SELF_LINK= "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/ApplicationInformation/01/TimeConfiguration/183";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final String DUMMY_STRING= "test1";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@BeforeEach
	public void initTestData() {
		applicationInformationRepository.deleteAllInBatch();
		applicationInformationRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SELF_LINK);
		UUID foundUuid = applicationInformationRepository.findById(presentUuid).map(ApplicationInformation::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}
	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<ApplicationInformation> application = applicationInformationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			application.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, application.map(ApplicationInformation::getUuid).orElse(null))
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
		//TODO write mapping test for subscriptions
		ApplicationInformation fullyMapped = applicationInformationRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMapped != null);

		Function<ApplicationInformation, Optional<Set<ApplicationInformationGrantTypes>>> grantTypeMap = ai -> Optional.ofNullable(ai.getGrantTypes());
		Function<ApplicationInformation,Optional<Set<ApplicationInformationScope>>> scopeType = ai -> Optional.ofNullable(ai.getApplicationScope());
		Function<ApplicationInformation, Optional<ApplicationInformation>> grantTypeMapReverse= grantTypeMap.andThen(opt->opt.flatMap(grantType->grantType.stream().findFirst().map(ApplicationInformationGrantTypes::getApplicationInformationGT)));
		Function<ApplicationInformation,Optional<ApplicationInformation>> scopeTypeReverse = scopeType.andThen(opt->opt.flatMap(scope->scope.stream().findFirst().map(ApplicationInformationScope::getApplicationInformationS)));

		Assertions.assertAll(
			"Entity mapping failures for forward " + fullyMapped.getUuid(),
			Stream.of(grantTypeMap, scopeType,grantTypeMapReverse,scopeTypeReverse)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMapped).isPresent()))
		);
	}


//TODO test mapping to subscriptions
	private static List<ApplicationInformation> buildTestData()
	{
		//why is it trying to join to reading quality, there is no reference to that
		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();
		List<ApplicationInformation> applicationInformations = Arrays.asList(ApplicationInformation.builder()
			.published(LocalDateTime.parse("2011-07-03 12:09:38", SQL_FORMATTER))
			.selfLinkHref(SELF_LINK)
			.selfLinkRel("self1")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/ApplicationInformation/01/IntervalBlock")
			.upLinkRel("up")
			.updated(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
			.authorizationServerAuthorizationEndpoint(DUMMY_STRING)
			.authorizationServerRegistrationEndpoint(DUMMY_STRING)
				.authorizationServerTokenEndpoint(DUMMY_STRING)
				.authorizationServerURI(DUMMY_STRING)
				.clientId(DUMMY_STRING)
				.clientIdIssuedAt(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
				.clientSecretExpiresAt(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
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
				.thirdPartyScopeSelectionScreenURI(DUMMY_STRING)
				.dataCustodianScopeSelectionScreenURI(DUMMY_STRING)
				/*.grantTypes(Stream.of(
					new ApplicationInformationGrantTypes()
						.setGrantTypes(GrantTypes.AUTHORIZATION_CODE),
					new ApplicationInformationGrantTypes()
						.setGrantTypes(GrantTypes.CLIENT_CREDENTIALS)
				).collect(Collectors.toSet()))*/
				.grantTypes(Stream.of(ApplicationInformationGrantTypes.builder()
						.grantTypes(GrantTypes.AUTHORIZATION_CODE)
					.build(),
					ApplicationInformationGrantTypes.builder()
						.grantTypes(GrantTypes.CLIENT_CREDENTIALS)
						.build()
				).collect(Collectors.toSet()))
				/*.applicationScope(Stream.of(
					new ApplicationInformationScope()
						.setScope("testestest"),
					new ApplicationInformationScope()
						.setScope("testestest2")
				).collect(Collectors.toSet()))*/
				.applicationScope(Stream.of(
					ApplicationInformationScope.builder()
						.scope("scope 1")
						.build(),
					ApplicationInformationScope.builder()
						.scope("scope 2")
						.build()
					).collect(Collectors.toSet())
				)
			.build(),
			ApplicationInformation.builder()
				.published(LocalDateTime.parse("2012-07-03 12:09:38", SQL_FORMATTER))
				.selfLinkHref(SELF_LINK)
				.selfLinkRel("self2")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/ApplicationInformation/02/IntervalBlock")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2013-03-02 05:00:00", SQL_FORMATTER))
				.authorizationServerAuthorizationEndpoint(DUMMY_STRING)
				.authorizationServerRegistrationEndpoint(DUMMY_STRING)
				.authorizationServerTokenEndpoint(DUMMY_STRING)
				.authorizationServerURI(DUMMY_STRING)
				.clientId(DUMMY_STRING)
				.clientIdIssuedAt(LocalDateTime.parse("2012-04-02 05:00:00", SQL_FORMATTER))
				.clientSecretExpiresAt(LocalDateTime.parse("2012-05-02 05:00:00", SQL_FORMATTER))
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
				.thirdPartyScopeSelectionScreenURI(DUMMY_STRING)
				.dataCustodianScopeSelectionScreenURI(DUMMY_STRING)
				.grantTypes(Stream.of(ApplicationInformationGrantTypes.builder()
						.grantTypes(GrantTypes.REFRESH_TOKEN)
						.build(),
					ApplicationInformationGrantTypes.builder()
						.grantTypes(GrantTypes.CLIENT_CREDENTIALS)
						.build()
				).collect(Collectors.toSet()))
				.applicationScope(Stream.of(
						ApplicationInformationScope.builder()
							.scope("scope 3")
							.build(),
						ApplicationInformationScope.builder()
							.scope("scope 4")
							.build()
					).collect(Collectors.toSet())
				)

				.build()
			);
		applicationInformations.forEach(ai->{

			ai.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ai.getSelfLinkHref()));
			ai.getApplicationScope().forEach(as -> {
				//as.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL,as.getScope()));
				as.setApplicationInformationS(ai);
			});
			ai.getGrantTypes().forEach(gt ->{

				//gt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL,gt.getApplicationInformationGT().toString()));
				gt.setApplicationInformationGT(ai);
				});

				ai.getGrantTypes();

			}

		);

		return applicationInformations;
	}
}
