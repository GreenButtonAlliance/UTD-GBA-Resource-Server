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
import org.greenbuttonalliance.gbaresourceserver.TestDataBuilder;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationRepositoryTest {
	private final ApplicationInformationRepository applicationInformationRepository;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void setup() {
		applicationInformationRepository.deleteAllInBatch();

		//Generate two ApplicationInformation objects
		var appInfo1 = TestDataBuilder.buildTestApplicationInformation(
			"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/135497",
			"GBA",
			"GBA Electricity & Gas");
		var appInfo2 = TestDataBuilder.buildTestApplicationInformation(
			"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/284915",
			"UtilityAPI",
			"Bluewater Power Distribution Corporation");

		//Save both ApplicationInformation objects to the repository
		applicationInformationRepository.saveAll(Arrays.asList(appInfo1, appInfo2));
	}

	@Test
	@DisplayName("Connection to Postgres container is established")
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	@DisplayName("ApplicationInformation Id present returns matching object")
	public void findByUuid_returnsCorrectApplicationInformation() {
		// Use the specific self-link HREFs to find the UUIDs
		UUID presentUuid1 = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL,
			"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/135497");
		UUID foundUuid1 = applicationInformationRepository.findById(presentUuid1).map(ApplicationInformation::getUuid).orElse(null);
		Assertions.assertEquals(presentUuid1, foundUuid1);

		// Repeat for the second ApplicationInformation object
		UUID presentUuid2 = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL,
			"https://localhost:8080/DataCustodian/espi/1_1/resource/ApplicationInformation/284915");
		UUID foundUuid2 = applicationInformationRepository.findById(presentUuid2).map(ApplicationInformation::getUuid).orElse(null);
		Assertions.assertEquals(presentUuid2, foundUuid2);
	}

	@Test
	@DisplayName("ApplicationInformation Id non-existing returns empty Optional")
	void findByNotPresentId_returnsEmpty() {
		// Generate a UUID that is guaranteed not to exist in the database
		UUID nonExistingUuid = UUID.randomUUID();

		// Attempt to find an ApplicationInformation by the non-existing UUID
		Optional<ApplicationInformation> result = applicationInformationRepository.findById(nonExistingUuid);

		// Assert that the result is empty
		Assertions.assertTrue(result.isEmpty(), "Expected an empty Optional when querying a non-existing UUID");
	}

	@Test
	@DisplayName("ApplicationInformation request with no Id returns all objects")
	public void findAll_returnsAll() {
		int findByAllSize = applicationInformationRepository.findAll().size();
		// Directly compare with the known number of ApplicationInformation objects added
		int expectedSize = 2; // Since we added two ApplicationInformation objects in setup

		Assertions.assertEquals(expectedSize, findByAllSize);
	}
}
