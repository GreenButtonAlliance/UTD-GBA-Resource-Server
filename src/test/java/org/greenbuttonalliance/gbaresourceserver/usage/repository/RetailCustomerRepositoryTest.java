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
import org.greenbuttonalliance.gbaresourceserver.common.model.AggregateNodeRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.PnodeRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.TariffRiderRef;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ServiceDeliveryPoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AmIBillingReadyKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.PhaseCodeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ServiceKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UsagePointConnectedKind;
import org.greenbuttonalliance.gbaresourceserver.usage.repository.UsagePointRepositoryTest;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RetailCustomerRepositoryTest {
	private final RetailCustomerRepository retailCustomerRepository;

	// for testing findById
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/RetailCustomer/08";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";

	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		retailCustomerRepository.deleteAllInBatch();
		retailCustomerRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = retailCustomerRepository.findById(presentUuid).map(RetailCustomer::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<RetailCustomer> readingType = retailCustomerRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			readingType.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, readingType.map(RetailCustomer::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = retailCustomerRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		RetailCustomer fullyMappedRetailCustomer = retailCustomerRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedRetailCustomer != null);

		Function<RetailCustomer, Optional<Set<UsagePoint>>> retailCustomerToUsagePoints = rc -> Optional.ofNullable(rc.getUsagePoints());

		Assertions.assertAll(
			"Entity mapping failures for retail customer " + fullyMappedRetailCustomer.getUuid(),
			Stream.of(retailCustomerToUsagePoints)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedRetailCustomer).isPresent()))
		);
	}

	private static List<RetailCustomer> buildTestData() {
		List<RetailCustomer> readingTypes = Arrays.asList(
			RetailCustomer.builder()
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
				.role("hatever")
				.username("Username")
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						UsagePointRepositoryTest.createUsageRepository()
					)))
				.build(),
			RetailCustomer.builder()
				.description("Hourly Wh Received")
				.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/1")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.enabled(Boolean.FALSE)
				.firstName("First")
				.lastName("last")
				.password("password")
				.role("whatever")
				.username("aUsername")
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						UsagePointRepositoryTest.createUsageRepository()
					)))
				.build(),
			RetailCustomer.builder()
				.description("Hourly Wh Delivered")
				.published(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/2")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.enabled(Boolean.TRUE)
				.firstName("First")
				.lastName("again")
				.password("password")
				.role("whatever")
				.username("aUsernam")
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						UsagePointRepositoryTest.createUsageRepository()
					)))
				.build()
		);

		// hydrate UUIDs and entity mappings

		AtomicInteger count = new AtomicInteger();
		readingTypes.forEach(rt -> {
			rt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rt.getSelfLinkHref()));

			UsagePoint up = rt.getUsagePoints().stream().toList().get(0);
			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
			up.setRetailCustomer(rt);
			up.setServiceDeliveryPoint(ServiceDeliveryPoint.builder()
				.name("name")
				.tariffProfile("tariffProfile")
				.customerAgreement("customerAgreement")
				.tariffRiderRefs(
					new HashSet<>(
						Collections.singletonList(
							TariffRiderRef.builder()
								.enrollmentStatus(EnrollmentStatus.ENROLLED)
								.effectiveDate(1L)
								.riderType("riderType")
								.build()
						)
					)
				)
				.build());

			count.getAndIncrement();
			UsagePointRepositoryTest.hydrateConnectedUsagePointEntities(up, count.toString());
			ServiceDeliveryPoint serviceDeliveryPoint = up.getServiceDeliveryPoint();
			serviceDeliveryPoint.setUsagePoints(new HashSet<>(
				Collections.singletonList(up)
			));

			TimeConfiguration tc = up.getTimeConfiguration();


			tc.setUsagePoints(new HashSet<>(
				Collections.singletonList(up)
			));
			// TODO hydrate MeterReading reference when available
		});
		return readingTypes;
	}

}
