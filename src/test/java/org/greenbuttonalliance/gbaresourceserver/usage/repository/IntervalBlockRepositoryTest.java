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
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalReading;
import org.greenbuttonalliance.gbaresourceserver.usage.model.QualityOfReading;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ReadingQuality;
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
public class IntervalBlockRepositoryTest {
	private final IntervalBlockRepository intervalBlockRepository;

	// for testing findById
	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";

	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		intervalBlockRepository.deleteAllInBatch();
		intervalBlockRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = intervalBlockRepository.findById(presentUuid).map(IntervalBlock::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<IntervalBlock> intervalBlock = intervalBlockRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			intervalBlock.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, intervalBlock.map(IntervalBlock::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = intervalBlockRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		IntervalBlock fullyMappedBlock = intervalBlockRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedBlock != null);

		// build functions to test mappings from IntervalBlock <--> IntervalReading <--> ReadingQuality
		Function<IntervalBlock, Optional<Set<IntervalReading>>> blockToReadings = ib -> Optional.ofNullable(ib.getIntervalReadings());
		Function<IntervalBlock, Optional<Set<ReadingQuality>>> blockToReadingQualities = blockToReadings.andThen(opt -> opt.flatMap(
			readings -> readings.stream().findFirst().map(IntervalReading::getReadingQualities)
		));
		Function<IntervalBlock, Optional<IntervalReading>> blockToReadingReversed = blockToReadingQualities.andThen(opt -> opt.flatMap(
			readingQualities -> readingQualities.stream().findFirst().map(ReadingQuality::getReading)
		));
		Function<IntervalBlock, Optional<IntervalBlock>> blockToBlockReversed = blockToReadingReversed.andThen(opt -> opt.map(
			IntervalReading::getBlock
		));

		Assertions.assertAll(
			"Entity mapping failures for block " + fullyMappedBlock.getUuid(),
			Stream.of(blockToReadings, blockToReadingQualities, blockToReadingReversed, blockToBlockReversed)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedBlock).isPresent()))
		);
	}

	// encapsulated in a method to make available to other test classes
	public static List<IntervalBlock> buildTestData() {
		List<IntervalBlock> intervalBlocks = Arrays.asList(
			IntervalBlock.builder()
				.published(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
				.start(1330578000L)
				.duration(1800L)
				.intervalReadings(Stream.of(
					new IntervalReading()
						.setCost(974L)
						.setStart(1330578000L)
						.setDuration(900L)
						.setValue(285L)
						.setReadingQualities(
							Stream.of(new ReadingQuality().setQuality(QualityOfReading.VALID),
									new ReadingQuality().setQuality(QualityOfReading.RAW),
									new ReadingQuality().setQuality(QualityOfReading.DERIVED))
								.collect(Collectors.toSet())),
					new IntervalReading()
						.setCost(965L)
						.setStart(1330578900L)
						.setDuration(900L)
						.setValue(383L)
						.setReadingQualities(
							Stream.of(new ReadingQuality().setQuality(QualityOfReading.OTHER),
									new ReadingQuality().setQuality(QualityOfReading.VALID))
								.collect(Collectors.toSet())))
					.collect(Collectors.toSet()))
				.build(),
			IntervalBlock.builder()
				.published(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
				.start(1330578800L)
				.duration(900L)
				.intervalReadings(Stream.of(
					new IntervalReading()
						.setCost(922L)
						.setStart(1330578800L)
						.setDuration(900L)
						.setValue(350L)
						.setReadingQualities(
							Stream.of(new ReadingQuality().setQuality(QualityOfReading.VALID))
								.collect(Collectors.toSet())))
					.collect(Collectors.toSet()))
				.build(),
			IntervalBlock.builder()
				.published(LocalDateTime.parse("2012-03-04 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/175")
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2012-03-04 05:00:00", SQL_FORMATTER))
				.start(1330987644L)
				.duration(900L)
				.intervalReadings(Collections.emptySet())
				.build()
		);

		// hydrate UUIDs and entity mappings
		intervalBlocks.forEach(ib -> {
			ib.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ib.getSelfLinkHref()));
			ib.getIntervalReadings().forEach(ir -> {
				ir.setBlock(ib);
				ir.getReadingQualities().forEach(rq -> rq.setReading(ir));
			});
		});
		return intervalBlocks;
	}
}
