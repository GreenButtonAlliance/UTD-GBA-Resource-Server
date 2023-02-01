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
import org.greenbuttonalliance.gbaresourceserver.usage.model.ElectricPowerQualitySummary;

import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.repository.ElectricPowerQualitySummaryRepository;
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
public class ElectricPowerQualitySummaryRepositoryITest {

	private final ElectricPowerQualitySummaryRepository electricPowerQualitySummaryRepository;

	private static final String upLinkHref = "https://{domain}/espi/1_1/resource/ElectricPowerQualitySummary";

	// for testing findById
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/ElectricPowerQualitySummary/174";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";

	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		electricPowerQualitySummaryRepository.deleteAllInBatch();
		electricPowerQualitySummaryRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = electricPowerQualitySummaryRepository.findById(presentUuid).map(ElectricPowerQualitySummary::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<ElectricPowerQualitySummary> electricPowerQualitySummary = electricPowerQualitySummaryRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			electricPowerQualitySummary.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, electricPowerQualitySummary.map(ElectricPowerQualitySummary::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = electricPowerQualitySummaryRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		ElectricPowerQualitySummary fullyMappedElectricPowerQualitySummary = electricPowerQualitySummaryRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedElectricPowerQualitySummary != null);

		Function<ElectricPowerQualitySummary, Optional<UsagePoint>> electricPowerQualitySummaryToUsagePoint = epqs -> Optional.ofNullable(epqs.getUsagePoint());

		Assertions.assertAll(
			"Entity mapping failures for electric power quality summary " + fullyMappedElectricPowerQualitySummary.getUuid(),
			Stream.of(electricPowerQualitySummaryToUsagePoint)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedElectricPowerQualitySummary).isPresent()))
		);
	}

	private static List<ElectricPowerQualitySummary> buildTestData() {
		List<ElectricPowerQualitySummary> electricPowerQualitySummaries = Arrays.asList(
			ElectricPowerQualitySummary.builder()
				.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.flickerPlt(1L)
				.flickerPst(2L)
				.harmonicVoltage(3L)
				.longInterruptions(4L)
				.mainsVoltage(5L)
				.measurementProtocol((short) 6)
				.powerFrequency(7L)
				.rapidVoltageChanges(8L)
				.shortInterruptions(9L)
				.summaryInterval(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.supplyVoltageDips(12L)
				.supplyVoltageImbalance(13L)
				.supplyVoltageVariations(14L)
				.tempOvervoltage(15L)
				.usagePoint(UsagePointRepositoryITest.createUsagePoint())
				.build(),
			ElectricPowerQualitySummary.builder()
				.published(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/ElectricPowerQualitySummary/175")
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.flickerPlt(1L)
				.flickerPst(2L)
				.harmonicVoltage(3L)
				.longInterruptions(4L)
				.mainsVoltage(5L)
				.measurementProtocol((short) 6)
				.powerFrequency(7L)
				.rapidVoltageChanges(8L)
				.shortInterruptions(9L)
				.summaryInterval(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.supplyVoltageDips(12L)
				.supplyVoltageImbalance(13L)
				.supplyVoltageVariations(14L)
				.tempOvervoltage(15L)
				.usagePoint(UsagePointRepositoryITest.createUsagePoint())
				.build(),
			ElectricPowerQualitySummary.builder()
				.published(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/ElectricPowerQualitySummary/176")
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.flickerPlt(1L)
				.flickerPst(2L)
				.harmonicVoltage(3L)
				.longInterruptions(4L)
				.mainsVoltage(5L)
				.measurementProtocol((short) 6)
				.powerFrequency(7L)
				.rapidVoltageChanges(8L)
				.shortInterruptions(9L)
				.summaryInterval(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
				.supplyVoltageDips(12L)
				.supplyVoltageImbalance(13L)
				.supplyVoltageVariations(14L)
				.tempOvervoltage(15L)
				.usagePoint(UsagePointRepositoryITest.createUsagePoint())
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		electricPowerQualitySummaries.forEach(epqs -> {

			epqs.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, epqs.getSelfLinkHref()));

			UsagePoint up = epqs.getUsagePoint();
			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
			up.setElectricPowerQualitySummaries(new HashSet<>(
				Collections.singletonList(
					epqs
				)));

			count.getAndIncrement();

			UsagePointRepositoryITest.hydrateConnectedUsagePointEntities(up, count.toString());

			UsagePointRepositoryITest.connectUsagePoint(up);
		});

		return electricPowerQualitySummaries;
	}
}
