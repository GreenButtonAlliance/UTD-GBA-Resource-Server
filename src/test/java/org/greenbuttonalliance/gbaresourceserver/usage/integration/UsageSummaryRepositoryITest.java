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
import org.greenbuttonalliance.gbaresourceserver.usage.repository.UsageSummaryRepository;
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
public class UsageSummaryRepositoryITest {

	private final UsageSummaryRepository usageSummaryRepository;

	// for testing findById
	private static final String upLinkHref = "https://{domain}/espi/1_1/resource/UsageSummary";
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/UsageSummary/174";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		usageSummaryRepository.deleteAllInBatch();
		usageSummaryRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = usageSummaryRepository.findById(presentUuid).map(UsageSummary::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<UsageSummary> usageSummary = usageSummaryRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			usageSummary.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, usageSummary.map(UsageSummary::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = usageSummaryRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		UsageSummary fullyMappedUsageSummary = usageSummaryRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedUsageSummary != null);

		Function<UsageSummary, Optional<Set<LineItem>>> usageSummaryToLineItems = us -> Optional.ofNullable(us.getLineItems());
		Function<UsageSummary, Optional<UsagePoint>> usageSummaryToUsagePoint = us -> Optional.ofNullable(us.getUsagePoint());

		Assertions.assertAll(
			"Entity mapping failures for usage summary " + fullyMappedUsageSummary.getUuid(),
			Stream.of(usageSummaryToLineItems,
					usageSummaryToUsagePoint)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedUsageSummary).isPresent()))
		);
	}

	private static List<UsageSummary> buildTestData() {
		List<UsageSummary> usageSummaries = Arrays.asList(
			UsageSummary.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.billingPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.billLastPeriod(1L)
				.billToDate(1L)
				.costAdditionalLastPeriod(1L)
				.lineItems(new HashSet<>(List.of(
					LineItem.builder()
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
						.build(),

					LineItem.builder()
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
						.build(),

					LineItem.builder()
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
						.build()
					)
					)
				)
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
				.build(),

			UsageSummary.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/UsageSummary/175")
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.billingPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.billLastPeriod(1L)
				.billToDate(1L)
				.costAdditionalLastPeriod(1L)
				.lineItems(new HashSet<>(List.of(
						LineItem.builder()
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
							.build(),

						LineItem.builder()
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
							.build(),

						LineItem.builder()
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
							.build()
					)
					)
				)
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
				.build(),

			UsageSummary.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/UsageSummary/176")
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.billingPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.billLastPeriod(1L)
				.billToDate(1L)
				.costAdditionalLastPeriod(1L)
				.lineItems(new HashSet<>(List.of(
					LineItem.builder()
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
						.build(),

						LineItem.builder()
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
							.build(),

						LineItem.builder()
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
							.build()
					))
				)
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
				.tariffRiderRefs(new HashSet<>(Collections.singletonList(
					TariffRiderRef.builder()
						.enrollmentStatus(EnrollmentStatus.ENROLLED)
						.effectiveDate(1L)
						.riderType("riderType")
						.build()
					))
				)
				.billingChargeSource(new BillingChargeSource()
					.setAgencyName("agencyName"))
				.usagePoint(UsagePointRepositoryITest.createUsagePoint())
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		usageSummaries.forEach(us -> {
			us.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, us.getSelfLinkHref()));

			us.getLineItems().forEach(li -> {

				count.getAndIncrement();

//				li.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, "UUID"+count));
				li.setUsageSummary(us);
			});

			UsagePoint up = us.getUsagePoint();
			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
			up.setUsageSummaries(new HashSet<>(
				Collections.singletonList(
					us
				)));

			count.getAndIncrement();

			UsagePointRepositoryITest.hydrateConnectedUsagePointEntities(up, count.toString());

			UsagePointRepositoryITest.connectUsagePoint(up);

			});

		return usageSummaries;
	}
}
