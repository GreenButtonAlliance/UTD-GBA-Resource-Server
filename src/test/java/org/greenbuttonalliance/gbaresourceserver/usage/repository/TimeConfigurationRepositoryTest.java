/*
 * Copyright (c) 2022-2024 Green Button Alliance, Inc.
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
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeConfigurationRepositoryTest {
	private final TimeConfigurationRepository timeConfigurationRepository;
	private static final String SELF_LINK= "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/183";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		timeConfigurationRepository.deleteAllInBatch();
		timeConfigurationRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SELF_LINK);
		UUID foundUuid = timeConfigurationRepository.findById(presentUuid).map(TimeConfiguration::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<TimeConfiguration> timeConfiguration = timeConfigurationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			timeConfiguration.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, timeConfiguration.map(TimeConfiguration::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = timeConfigurationRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		TimeConfiguration fullyMappedTimeConfiguration = timeConfigurationRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedTimeConfiguration != null);

		Function<TimeConfiguration, Optional<Set<UsagePoint>>> timeConfigurationToUsagePoints = tc -> Optional.ofNullable(tc.getUsagePoints());

		Assertions.assertTrue(timeConfigurationToUsagePoints.apply(fullyMappedTimeConfiguration).isPresent());
	}

	private static List<TimeConfiguration> buildTestData() {
		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();
		List<TimeConfiguration> timeConfigurations =  Arrays.asList(TimeConfiguration.builder()
			.selfLinkHref(SELF_LINK)
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
			.dstEndRule(deadbeefs)
			.dstOffset(100L)
			.dstStartRule(deadbeefs)
			.tzOffset(10L)
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						TestUtils.createUsagePoint()
					)))
				.build(),
		TimeConfiguration.builder()
			.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/184")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
			.dstEndRule(deadbeefs)
			.dstOffset(200L)
			.dstStartRule(deadbeefs)
			.tzOffset(20L)
			.usagePoints(new HashSet<>(
				Collections.singletonList(
					TestUtils.createUsagePoint()
				)))
			.build(),
		TimeConfiguration.builder()
			.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/185")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
			.dstEndRule(deadbeefs)
			.dstOffset(300L)
			.dstStartRule(deadbeefs)
			.tzOffset(30L)
			.usagePoints(new HashSet<>(
				Collections.singletonList(
					TestUtils.createUsagePoint()
				)))
			.build()
		);

		AtomicInteger count = new AtomicInteger();
		timeConfigurations.forEach(tc->{
			tc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, tc.getSelfLinkHref()));

			UsagePoint up = tc.getUsagePoints().stream().toList().get(0);
			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
			up.setTimeConfiguration(tc);

			count.getAndIncrement();

			TestUtils.hydrateConnectedUsagePointEntities(up, count.toString());

			TestUtils.connectUsagePoint(up);

		});
		return timeConfigurations;
	}
}

