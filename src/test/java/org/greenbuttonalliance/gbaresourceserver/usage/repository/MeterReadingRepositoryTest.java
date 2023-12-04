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
import org.greenbuttonalliance.gbaresourceserver.TestUtils;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.model.MeterReading;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ReadingType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AccumulationKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.CommodityKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataQualifierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.FlowDirectionKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.MeasurementKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.PhaseCodeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TimeAttributeKind;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadingRepositoryTest {
	private final MeterReadingRepository meterReadingRepository;

	// for testing findById
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		meterReadingRepository.deleteAllInBatch();
		meterReadingRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = meterReadingRepository.findById(presentUuid).map(MeterReading::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<MeterReading> meterReading = meterReadingRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			meterReading.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, meterReading.map(MeterReading::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = meterReadingRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		MeterReading fullyMappedMeterReading = meterReadingRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedMeterReading != null);

		Function<MeterReading, Optional<ReadingType>> meterReadingToReadingType = mr -> Optional.ofNullable(mr.getReadingType());
		Function<MeterReading, Optional<Set<IntervalBlock>>> meterReadingToIntervalBlocks = mr -> Optional.ofNullable(mr.getIntervalBlocks());
		Function<MeterReading, Optional<UsagePoint>> meterReadingToUsagePoint = mr -> Optional.ofNullable(mr.getUsagePoint());

		Assertions.assertAll(
			"Entity mapping failures for meter reading " + fullyMappedMeterReading.getUuid(),
			Stream.of(meterReadingToReadingType,
					meterReadingToIntervalBlocks,
					meterReadingToUsagePoint)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedMeterReading).isPresent()))
		);
	}

	private static List<MeterReading> buildTestData() {
		List<MeterReading> meterReadings = Arrays.asList(
			MeterReading.builder()
				.description("Fifteen Minute Electricity Consumption")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
				.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.readingType(ReadingType.builder()
					.description("Type of Meter Reading Data")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref(PRESENT_SELF_LINK)
					.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.accumulationBehavior(AccumulationKind.DELTA_DATA)
					.commodity(CommodityKind.ELECTRICITY_SECONDARY_METERED)
					.consumptionTier(null)
					.currency(Currency.USD)
					.dataQualifier(DataQualifierKind.NORMAL)
					.defaultQuality(null)
					.flowDirection(FlowDirectionKind.FORWARD)
					.intervalLength(900L)
					.kind(MeasurementKind.ENERGY)
					.phase(PhaseCodeKind.S12N)
					.powerOfTenMultiplier(UnitMultiplierKind.NONE)
					.timeAttribute(TimeAttributeKind.NONE)
					.tou(null)
					.uom(UnitSymbolKind.W_H)
					.cpp(null)
					.interharmonicNumerator(600L)
					.interharmonicDenominator(800L)
					.measuringPeriod(null)
					.argumentNumerator(1L)
					.argumentDenominator(2L)
					.build())
				.intervalBlocks(Stream.of(
					IntervalBlock.builder()
						.published(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
						.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173")
						.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
						.updated(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
						.build(),
					IntervalBlock.builder()
						.published(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
						.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174")
						.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
						.updated(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
						.build())
					.collect(Collectors.toSet()))
				.usagePoint(TestUtils.createUsagePoint())
				.build(),
			MeterReading.builder()
				.description("Hourly Wh Received")
				.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading/1")
				.upLinkHref("DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading")
				.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.readingType(ReadingType.builder()
					.description("Hourly Wh Received")
					.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/ReadingType/1")
					.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.accumulationBehavior(AccumulationKind.DELTA_DATA)
					.commodity(CommodityKind.ELECTRICITY_SECONDARY_METERED)
					.consumptionTier(null)
					.currency(Currency.USD)
					.dataQualifier(DataQualifierKind.NORMAL)
					.defaultQuality(null)
					.flowDirection(FlowDirectionKind.FORWARD)
					.intervalLength(3600L)
					.kind(MeasurementKind.ENERGY)
					.phase(PhaseCodeKind.S12N)
					.powerOfTenMultiplier(UnitMultiplierKind.NONE)
					.timeAttribute(null)
					.tou(null)
					.uom(UnitSymbolKind.W_H)
					.cpp(null)
					.interharmonicNumerator(null)
					.interharmonicDenominator(null)
					.measuringPeriod(null)
					.argumentNumerator(null)
					.argumentDenominator(null)
					.build())
				.intervalBlocks(Stream.of(
					IntervalBlock.builder()
						.published(LocalDateTime.parse("2012-03-04 05:00:00", SQL_FORMATTER))
						.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/175")
						.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
						.updated(LocalDateTime.parse("2012-03-04 05:00:00", SQL_FORMATTER))
						.build())
					.collect(Collectors.toSet()))

				.usagePoint(TestUtils.createUsagePoint())
				.build(),
			MeterReading.builder()
				.description("Hourly Wh Delivered")
				.published(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading/2")
				.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading")
				.updated(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.readingType(null)
				.intervalBlocks(Collections.emptySet())
				.usagePoint(TestUtils.createUsagePoint())
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		meterReadings.forEach(mr -> {

			mr.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, mr.getSelfLinkHref()));

			mr.getIntervalBlocks().forEach(ib -> {
				ib.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ib.getSelfLinkHref()));
				ib.setMeterReading(mr);
			});

			Optional.ofNullable(mr.getReadingType()).ifPresent(rt -> {
				rt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rt.getSelfLinkHref()));
				rt.setMeterReading(mr);
			});

			UsagePoint up = mr.getUsagePoint();
			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
			up.setMeterReadings(new HashSet<>(
				Collections.singletonList(
					mr
				)));

			count.getAndIncrement();

			TestUtils.hydrateConnectedUsagePointEntities(up, count.toString());

			TestUtils.connectUsagePoint(up);
		});
		return meterReadings;
	}
}
