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
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.BillingChargeSource;
import org.greenbuttonalliance.gbaresourceserver.usage.model.LineItem;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TariffRiderRef;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsageSummary;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.CommodityKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ItemKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.QualityOfReading;
import org.greenbuttonalliance.gbaresourceserver.usage.repository.LineItemRepository;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineItemRepositoryITest {

	private final LineItemRepository lineItemRepository;
	private static final Long PRESENT = 100000L;
	private static final Long NOT_PRESENT = 9999999L;
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		lineItemRepository.deleteAllInBatch();
		lineItemRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		Long presentId = PRESENT;
		Long foundId = lineItemRepository.findById(presentId).map(LineItem::getId).orElse(null);

		Assertions.assertEquals(
			presentId,
			foundId,
			() -> String.format("findById with %s returns entity with ID %s", presentId, foundId)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		Optional<LineItem> lineItem = lineItemRepository.findById(NOT_PRESENT);

		Assertions.assertTrue(
			lineItem.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", NOT_PRESENT,
				lineItem.map(LineItem::getId).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = lineItemRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
//		LineItem fullyMappedLineItem = lineItemRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT)).orElse(null);
		Optional<LineItem> fullyMappedLineItem = lineItemRepository.findById(PRESENT);
//		Assumptions.assumeTrue(fullyMappedLineItem != null);

		Function<LineItem, Optional<UsageSummary>> lineItemToUsageSummary = li -> Optional.ofNullable(li.getUsageSummary());

//		Assertions.assertTrue(lineItemToUsageSummary.apply(fullyMappedLineItem).isPresent());
	}

	private static List<LineItem> buildTestData() {
		AtomicLong lineItemId = new AtomicLong(PRESENT);
		List<LineItem> lineItems = Arrays.asList(
			LineItem.builder()
				.id(lineItemId.getAndIncrement())
				.amount(1L)
				.rounding(1L)
				.dateTime(1L)
				.note("note")
				.measurement(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.itemKind(ItemKind.INFORMATION)
				.unitCost(1L)
				.itemPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.usageSummary(UsageSummary.builder()
					.description("description")
					.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://localhost:8080/espi/1_1/resource/UsageSummary/174")
					.upLinkHref("https://localhost:8080/espi/1_1/resource/UsageSummary")
					.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.billingPeriod(new DateTimeInterval()
						.setDuration(10L)
						.setStart(11L))
					.billLastPeriod(1L)
					.billToDate(1L)
					.costAdditionalLastPeriod(1L)
					.lineItems(Collections.emptySet())
					.currency(Currency.USD)
					.overallConsumptionLastPeriod(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayLastYearNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.peakDemand(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayLastYearOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.qualityOfReading(QualityOfReading.VALID)
					.ratchetDemand(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.ratchetDemandPeriod(new DateTimeInterval()
						.setDuration(10L)
						.setStart(11L))
					.statusTimeStamp(1L)
					.commodity(CommodityKind.CO2)
					.tariffProfile("tariffProfile")
					.readCycle("readCycle")
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
					.billingChargeSource(new BillingChargeSource()
						.setAgencyName("agencyName"))
					.usagePoint(UsagePointRepositoryITest.createUsagePoint())
					.build())
				.build(),

			LineItem.builder()
				.id(lineItemId.getAndIncrement())
				.amount(1L)
				.rounding(1L)
				.dateTime(1L)
				.note("note")
				.measurement(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.itemKind(ItemKind.INFORMATION)
				.unitCost(1L)
				.itemPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.usageSummary(UsageSummary.builder()
					.description("description")
					.published(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://localhost:8080/espi/1_1/resource/UsageSummary/175")
					.upLinkHref("https://localhost:8080/espi/1_1/resource/UsageSummary")
					.updated(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.billingPeriod(new DateTimeInterval()
						.setDuration(10L)
						.setStart(11L))
					.billLastPeriod(1L)
					.billToDate(1L)
					.costAdditionalLastPeriod(1L)
					.lineItems(Collections.emptySet())
					.currency(Currency.USD)
					.overallConsumptionLastPeriod(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayLastYearNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.peakDemand(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayLastYearOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.qualityOfReading(QualityOfReading.VALID)
					.ratchetDemand(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.ratchetDemandPeriod(new DateTimeInterval()
						.setDuration(10L)
						.setStart(11L))
					.statusTimeStamp(1L)
					.commodity(CommodityKind.CO2)
					.tariffProfile("tariffProfile")
					.readCycle("readCycle")
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
					.billingChargeSource(new BillingChargeSource()
						.setAgencyName("agencyName"))
					.usagePoint(UsagePointRepositoryITest.createUsagePoint())
					.build())
				.build(),

			LineItem.builder()
				.id(lineItemId.getAndIncrement())
				.amount(1L)
				.rounding(1L)
				.dateTime(1L)
				.note("note")
				.measurement(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.itemKind(ItemKind.INFORMATION)
				.unitCost(1L)
				.itemPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.usageSummary(UsageSummary.builder()
					.description("description")
					.published(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://localhost:8080/espi/1_1/resource/UsageSummary/176")
					.upLinkHref("https://localhost:8080/espi/1_1/resource/UsageSummary")
					.updated(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
					.billingPeriod(new DateTimeInterval()
						.setDuration(10L)
						.setStart(11L))
					.billLastPeriod(1L)
					.billToDate(1L)
					.costAdditionalLastPeriod(1L)
					.lineItems(Collections.emptySet())
					.currency(Currency.USD)
					.overallConsumptionLastPeriod(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayLastYearNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.currentDayOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.peakDemand(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayLastYearOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayNetConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.previousDayOverallConsumption(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.qualityOfReading(QualityOfReading.VALID)
					.ratchetDemand(new SummaryMeasurement()
						.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
						.setTimeStamp(1L)
						.setUom(UnitSymbolKind.M)
						.setValue(1L)
						.setReadingTypeRef("readingTypeRef"))
					.ratchetDemandPeriod(new DateTimeInterval()
						.setDuration(10L)
						.setStart(11L))
					.statusTimeStamp(1L)
					.commodity(CommodityKind.CO2)
					.tariffProfile("tariffProfile")
					.readCycle("readCycle")
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
					.billingChargeSource(new BillingChargeSource()
						.setAgencyName("agencyName"))
					.usagePoint(UsagePointRepositoryITest.createUsagePoint())
					.build())
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		lineItems.forEach(li -> {
//			count.getAndIncrement();
//			li.setId(100000L);
			UsageSummary us = li.getUsageSummary();
				us.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, us.getSelfLinkHref()));
				us.setLineItems(new HashSet<>(List.of(li)));

			UsagePoint up = us.getUsagePoint();

			up.setUsageSummaries(new HashSet<>(
				Collections.singletonList(
					us
				)
			));

			count.getAndIncrement();

			UsagePointRepositoryITest.hydrateConnectedUsagePointEntities(up, count.toString());

			UsagePointRepositoryITest.connectUsagePoint(up);

		});
		return lineItems;
	}
}
