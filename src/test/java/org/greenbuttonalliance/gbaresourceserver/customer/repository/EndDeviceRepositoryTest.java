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

package org.greenbuttonalliance.gbaresourceserver.customer.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.customer.model.AcceptanceTest;
import org.greenbuttonalliance.gbaresourceserver.customer.model.ElectronicAddress;
import org.greenbuttonalliance.gbaresourceserver.customer.model.EndDevice;
import org.greenbuttonalliance.gbaresourceserver.customer.model.LifecycleDate;
import org.greenbuttonalliance.gbaresourceserver.customer.model.PerCent;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Status;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EndDeviceRepositoryTest {

	@Autowired
	private EndDeviceRepository endDeviceRepository;
	private static final String upLinkHref = "https://localhost:8080/DataCustodian/espi/1_1/resource/EndDevice";

	// for testing findById
	private static final String PRESENT_SELF_LINK =
		"https://localhost:8080/DataCustodian/espi/1_1/" +
			"resource/EndDevice/174357";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		endDeviceRepository.deleteAllInBatch();
		endDeviceRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = endDeviceRepository.findById(presentUuid).map(EndDevice::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<EndDevice> endDevice = endDeviceRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			endDevice.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, endDevice.map(EndDevice::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = endDeviceRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		EndDevice fullyMappedEndDevice = endDeviceRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedEndDevice != null);

		// first-level mappings; directly from fullyMappedEndDevice
		Function<EndDevice, Optional<ElectronicAddress>> endDeviceToElectronicAddress = ed ->
			Optional.ofNullable(ed.getElectronicAddress());
		Function<EndDevice, Optional<Status>> endDeviceToStatus = ed -> Optional.ofNullable(ed.getStatus());

		Assertions.assertAll(
			STR."Entity mapping failures for customer account \{fullyMappedEndDevice.getUuid()}",
			Stream.of(endDeviceToElectronicAddress,
					endDeviceToStatus)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedEndDevice).isPresent()))
		);
	}

	private static List<EndDevice> buildTestData() {
		//TODO: Add ServiceLocation to test EndDevice to ServiceLocation mapping
		List<EndDevice> endDevices = Arrays.asList(
			EndDevice.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.type("type")
				.utcNumber("utcNumber")
				.serialNumber("serialNumber")
				.lotNumber("lotNumber")
				.purchasePrice(1L)
				.critical(true)
				.electronicAddress(ElectronicAddress.builder()
					.lan("lan")
					.build())
				.lifecycle(LifecycleDate.builder()
					.manufacturedDate(1L)
					.build())
				.acceptanceTest(AcceptanceTest.builder()
					.type("type")
					.build())
				.initialCondition("initialCondition")
				.initialLossOfLife(PerCent.builder()
					.percent(0)
					.build())
				.status(Status.builder()
					.value("value")
					.build())
				.isVirtual(true)
				.isPan(true)
				.installCode("installCode")
				.amrSystem("amrSystem")
				.build(),

			EndDevice.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://localhost:8080/DataCustodian/espi/1_1/resource/EndDevice/175358")
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.type("type")
				.utcNumber("utcNumber")
				.serialNumber("serialNumber")
				.lotNumber("lotNumber")
				.purchasePrice(1L)
				.critical(true)
				.electronicAddress(ElectronicAddress.builder()
					.lan("lan")
					.build())
				.lifecycle(LifecycleDate.builder()
					.manufacturedDate(1L)
					.build())
				.acceptanceTest(AcceptanceTest.builder()
					.type("type")
					.build())
				.initialCondition("initialCondition")
				.initialLossOfLife(PerCent.builder()
					.percent(0)
					.build())
				.status(Status.builder()
					.value("value")
					.build())
				.isVirtual(true)
				.isPan(true)
				.installCode("installCode")
				.amrSystem("amrSystem")
				.build(),

			EndDevice.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://localhost:8080/DataCustodian/espi/1_1/resource/EndDevice/176359")
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.type("type")
				.utcNumber("utcNumber")
				.serialNumber("serialNumber")
				.lotNumber("lotNumber")
				.purchasePrice(1L)
				.critical(true)
				.electronicAddress(ElectronicAddress.builder()
					.lan("lan")
					.build())
				.lifecycle(LifecycleDate.builder()
					.manufacturedDate(1L)
					.build())
				.acceptanceTest(AcceptanceTest.builder()
					.type("type")
					.build())
				.initialCondition("initialCondition")
				.initialLossOfLife(PerCent.builder()
					.percent(0)
					.build())
				.status(Status.builder()
					.value("value")
					.build())
				.isVirtual(true)
				.isPan(true)
				.installCode("installCode")
				.amrSystem("amrSystem")
				.build()
		);

		// hydrate UUIDs
		endDevices.forEach(ed -> ed.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ed.getSelfLinkHref())));
		return endDevices;
	}
}
