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
import org.greenbuttonalliance.gbaresourceserver.common.model.BillingChargeSource;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.TariffRiderRef;
import org.greenbuttonalliance.gbaresourceserver.usage.model.LineItem;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsageSummary;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.CommodityKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.QualityOfReading;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitSymbolKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsageSummaryRepositoryTest {

	private final UsageSummaryRepository usageSummaryRepository;

	// for testing findById
	private static final String UUID_PARAMETER = "USTest";
	private static final String PRESENT = "USTest1";

	private static final String NOT_PRESENT = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		usageSummaryRepository.deleteAllInBatch();
		usageSummaryRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT);
		UUID foundUuid = usageSummaryRepository.findById(presentUuid).map(UsageSummary::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT);
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

	public static List<UsageSummary> buildTestData() {
		List<UsageSummary> usageSummaries = Arrays.asList(
			UsageSummary.builder()
				.billingPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.billLastPeriod(1L)
				.billToDate(1L)
				.costAdditionalLastPeriod(1L)
				.currency(Currency.USD)
				.overallConsumptionLastPeriod(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentDayLastYearNetConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentDayNetConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentDayOverallConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.peakDemand(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.previousDayLastYearOverallConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.previousDayNetConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.previousDayOverallConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.qualityOfReading(QualityOfReading.VALID)
				.ratchetDemand(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.ratchetDemandPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.statusTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.commodity(CommodityKind.CO2)
				.tariffProfile("tariffProfile")
				.readCycle("readCycle")
				.tariffRiderRefs(
					new HashSet<>(
						Arrays.asList(
							TariffRiderRef.builder()
								.enrollmentStatus(EnrollmentStatus.ENROLLED)
									.effectiveDate(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
										.riderType("riderType")
								.build()
						)
					)
				)
				.billingChargeSource(new BillingChargeSource()
					.setAgencyName("agencyName"))
				.build(),

			UsageSummary.builder()
				.billingPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.billLastPeriod(1L)
				.billToDate(1L)
				.costAdditionalLastPeriod(1L)
				.currency(Currency.USD)
				.overallConsumptionLastPeriod(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentDayLastYearNetConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentDayNetConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.currentDayOverallConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.peakDemand(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.previousDayLastYearOverallConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.previousDayNetConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.previousDayOverallConsumption(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.qualityOfReading(QualityOfReading.VALID)
				.ratchetDemand(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.ratchetDemandPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.statusTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.commodity(CommodityKind.CO2)
				.tariffProfile("tariffProfile")
				.readCycle("readCycle")
				.tariffRiderRefs(
					new HashSet<>(
						Arrays.asList(
							TariffRiderRef.builder()
								.enrollmentStatus(EnrollmentStatus.ENROLLED)
								.effectiveDate(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
								.riderType("riderType")
								.build()
						)
					)
				)
				.billingChargeSource(new BillingChargeSource()
					.setAgencyName("agencyName"))
				.build(),

							UsageSummary.builder()
								.billingPeriod(new DateTimeInterval()
									.setDuration(10L)
									.setStart(11L))
								.billLastPeriod(1L)
								.billToDate(1L)
								.costAdditionalLastPeriod(1L)
								.currency(Currency.USD)
								.overallConsumptionLastPeriod(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.currentDayLastYearNetConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.currentDayNetConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.currentDayOverallConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.peakDemand(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.previousDayLastYearOverallConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.previousDayNetConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.previousDayOverallConsumption(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.qualityOfReading(QualityOfReading.VALID)
								.ratchetDemand(new SummaryMeasurement()
									.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
									.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
									.setUom(UnitSymbolKind.M)
									.setValue(1L)
									.setReadingTypeRef("readingTypeRef"))
								.ratchetDemandPeriod(new DateTimeInterval()
									.setDuration(10L)
									.setStart(11L))
								.statusTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
								.commodity(CommodityKind.CO2)
								.tariffProfile("tariffProfile")
								.readCycle("readCycle")
								.tariffRiderRefs(
									new HashSet<>(
										Arrays.asList(
											TariffRiderRef.builder()
												.enrollmentStatus(EnrollmentStatus.ENROLLED)
												.effectiveDate(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
												.riderType("riderType")
												.build()
						)
					)
				)
				.billingChargeSource(new BillingChargeSource()
					.setAgencyName("agencyName"))
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		usageSummaries.forEach(us -> {
			count.getAndIncrement();
			us.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, UUID_PARAMETER+count));
		});

		return usageSummaries;
	}
}
