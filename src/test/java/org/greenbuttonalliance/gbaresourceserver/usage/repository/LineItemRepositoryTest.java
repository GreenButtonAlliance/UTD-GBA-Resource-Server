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
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.usage.model.LineItem;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ItemKind;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineItemRepositoryTest {

	private final LineItemRepository lineItemRepository;

	// for testing findById
	private static final String UUID_PARAMETER = "LITest";
	private static final String PRESENT = "LITest1";

	private static final String NOT_PRESENT = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		lineItemRepository.deleteAllInBatch();
		lineItemRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT);
		UUID foundUuid = lineItemRepository.findById(presentUuid).map(LineItem::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT);
		Optional<LineItem> lineItem = lineItemRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			lineItem.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, lineItem.map(LineItem::getUuid).orElse(null))
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

	public static List<LineItem> buildTestData() {
		List<LineItem> lineItems = Arrays.asList(
			LineItem.builder()
				.amount(1L)
				.rounding(1L)
				.dateTime(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.note("note")
				.measurement(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.itemKind(ItemKind.INFORMATION)
				.unitCost(1L)
				.itemPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
//				.usageSummary()
				.build(),

			LineItem.builder()
				.amount(1L)
				.rounding(1L)
				.dateTime(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.note("note")
				.measurement(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.itemKind(ItemKind.INFORMATION)
				.unitCost(1L)
				.itemPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
//				.usageSummary()
				.build(),

			LineItem.builder()
				.amount(1L)
				.rounding(1L)
				.dateTime(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.note("note")
				.measurement(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.itemKind(ItemKind.INFORMATION)
				.unitCost(1L)
				.itemPeriod(new DateTimeInterval()
					.setDuration(10L)
					.setStart(11L))
//				.usageSummary()
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		lineItems.forEach(li -> {
			count.getAndIncrement();
			li.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, UUID_PARAMETER+count));
		});

		return lineItems;
	}
}
