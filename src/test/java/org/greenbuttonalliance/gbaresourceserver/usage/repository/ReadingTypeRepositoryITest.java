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
import org.greenbuttonalliance.gbaresourceserver.usage.model.MeterReading;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ReadingType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AccumulationKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.CommodityKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataQualifierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.FlowDirectionKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.MeasurementKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.PhaseCodeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TimeAttributeKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReadingTypeRepositoryITest {
	private final ReadingTypeRepository readingTypeRepository;

	// for testing findById
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/ReadingType/07";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";

	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		readingTypeRepository.deleteAllInBatch();
		readingTypeRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = readingTypeRepository.findById(presentUuid).map(ReadingType::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<ReadingType> readingType = readingTypeRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			readingType.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, readingType.map(ReadingType::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = readingTypeRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		ReadingType fullyMappedReadingType = readingTypeRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedReadingType != null);

		Function<ReadingType, Optional<MeterReading>> readingTypeToMeterReading = rt -> Optional.ofNullable(rt.getMeterReading());

		Assertions.assertAll(
			"Entity mapping failures for reading type " + fullyMappedReadingType.getUuid(),
			Stream.of(readingTypeToMeterReading)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedReadingType).isPresent()))
		);
	}

	private static List<ReadingType> buildTestData() {
		List<ReadingType> readingTypes = Arrays.asList(
			ReadingType.builder()
				.description("Type of Meter Reading Data")
				.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
				.upLinkRel("up")
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
				.meterReading(MeterReading.builder()
					.description("Fifteen Minute Electricity Consumption")
					.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
					.usagePoint(UsagePointRepositoryITest.createUsagePoint())
					.build())
				.build(),
			ReadingType.builder()
				.description("Hourly Wh Received")
				.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/ReadingType/1")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
				.upLinkRel("up")
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
				.meterReading(MeterReading.builder()
					.description("Hourly Wh Received")
					.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading/1")
					.selfLinkRel("self")
					.upLinkHref("DataCustodian/espi/1_1/resource/RetailCustomer/1/UsagePoint/1/MeterReading")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.usagePoint(UsagePointRepositoryITest.createUsagePoint())
					.build())
				.build(),
			ReadingType.builder()
				.description("Hourly Wh Delivered")
				.published(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/ReadingType/2")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2013-05-28 07:00:00", SQL_FORMATTER))
				.accumulationBehavior(AccumulationKind.BULK_QUANTITY)
				.commodity(CommodityKind.ELECTRICITY_SECONDARY_METERED)
				.consumptionTier(null)
				.currency(Currency.USD)
				.dataQualifier(DataQualifierKind.NORMAL)
				.defaultQuality(null)
				.flowDirection(FlowDirectionKind.REVERSE)
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
				.meterReading(null)
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		readingTypes.forEach(rt -> {

			rt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rt.getSelfLinkHref()));

			Optional.ofNullable(rt.getMeterReading()).ifPresent(mr -> {
				mr.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, mr.getSelfLinkHref()));
				mr.setReadingType(rt);

				UsagePoint up = mr.getUsagePoint();

				up.setMeterReadings(new HashSet<>(
					Collections.singletonList(
						mr
					)
				));

				count.getAndIncrement();

				UsagePointRepositoryITest.hydrateConnectedUsagePointEntities(up, count.toString());

				UsagePointRepositoryITest.connectUsagePoint(up);

			});
		});
		return readingTypes;
	}
}
