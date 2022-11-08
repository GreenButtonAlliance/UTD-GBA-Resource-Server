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
	import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.QualityOfReading;
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
		Optional<Subscription> Subscription = subscriptionRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			Subscription.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, Subscription.map(Subscription::getUuid).orElse(null))
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

	/*
	APPLY test when the models for usagepoint, applicationInformation, and applicationInformationScopes are done.
	 */
//	@Test
//	public void entityMappings_areNotNull() {
//		Subscription fullyMappedSubscription = SubscriptionRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
//		Assumptions.assumeTrue(fullyMappedSubscription != null);
//
//		/* Test bidirectional mappings all the way from IntervalBlock <--> Application Information (IntervalReading) <--> Application Information Scopes (ReadingQuality) here since IntervalReading and ReadingQuality don't have
//		their own repositories for which we're testing their individual mappings */
//		Function<Subscription, Optional<Set<ApplicationInformation>>> subscriptionToApplicationInformation = ib -> Optional.ofNullable(ib.getApplicationInformation());
//		Function<Subscription, Optional<Set<ApplicationInformationScopes>>> subscriptionToApplicationInformationScopes = subscriptionToApplicationInformation.andThen(opt -> opt.flatMap(
//			readings -> readings.stream().findFirst().map(ApplicationInformation::getApplicationInformationScopes)
//		));
//		Function<Subscription, Optional<ApplicationInformation>> subscriptionToApplicationInformationReversed = subscriptionToApplicationInformationScopes.andThen(opt -> opt.flatMap(
//			applicationInformationScopes -> applicationInformationScopes.stream().findFirst().map(ApplicationInformationScopes::getScope)
//		));
//		Function<Subscription, Optional<Subscription>> subscriptionToSubscriptionReversed = subscriptionToApplicationInformationScopesReversed.andThen(opt -> opt.map(
//			ApplicationInformation::getClientId
//		));
//
//		Function<Subscription, Optional<UsagePoint>> subscriptionToUsagePoint = ib -> Optional.ofNullable(ib.getUsagepoint_id()); // this is potentially getUsagePoint, not getUsagepoint_id
//
//		Assertions.assertAll(
//			"Entity mapping failures for block " + fullyMappedSubscription.getUuid(),
//			Stream.of(SubscriptionToApplicationInformation,
//					SubscriptionToApplicationInformationScopes,
//					SubscriptionToApplicationInformationReversed,
//					SubscriptionToSubscriptionReversed,
//					SubscriptionToUsagePoint)
//				.map(mappingFunc ->
//					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedSubscription).isPresent()))
//		);
//	}
}
