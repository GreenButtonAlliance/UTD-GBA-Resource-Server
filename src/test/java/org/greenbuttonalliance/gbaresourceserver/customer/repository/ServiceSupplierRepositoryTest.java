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

package org.greenbuttonalliance.gbaresourceserver.customer.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Organisation;
import org.greenbuttonalliance.gbaresourceserver.customer.model.ServiceSupplier;
import org.greenbuttonalliance.gbaresourceserver.customer.model.enums.SupplierKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceSupplierRepositoryTest {
	@Autowired
	private ServiceSupplierRepository serviceSupplierRepository;

	// for testing findById
	private static final String PRESENT_ISSUER_IDENTIFICATION_NUMBER = "foo";
	private static final String NOT_PRESENT_ISSUER_IDENTIFICATION_NUMBER = "bar";

	@BeforeEach
	public void initTestData() {
		serviceSupplierRepository.deleteAllInBatch();
		serviceSupplierRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_ISSUER_IDENTIFICATION_NUMBER);
		UUID foundUuid = serviceSupplierRepository.findById(presentUuid).map(ServiceSupplier::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_ISSUER_IDENTIFICATION_NUMBER);
		Optional<ServiceSupplier> serviceSupplier = serviceSupplierRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			serviceSupplier.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, serviceSupplier.map(ServiceSupplier::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = serviceSupplierRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		ServiceSupplier fullyMappedServiceSupplier = serviceSupplierRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_ISSUER_IDENTIFICATION_NUMBER)).orElse(null);
		Assumptions.assumeTrue(fullyMappedServiceSupplier != null);

		Function<ServiceSupplier, Optional<Organisation>> serviceSupplierToOrganisation = ss -> Optional.ofNullable(ss.getOrganisation());

		Assertions.assertAll(
			"Entity mapping failures for service supplier " + fullyMappedServiceSupplier.getUuid(),
			Stream.of(serviceSupplierToOrganisation)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedServiceSupplier).isPresent()))
		);
	}

	private static List<ServiceSupplier> buildTestData() {
		List<ServiceSupplier> serviceSuppliers = Arrays.asList(
			ServiceSupplier.builder()
				.organisation(Organisation.builder()
					.organisationName("foo")
					.build())
				.kind(SupplierKind.RETAILER)
				.issuerIdentificationNumber(PRESENT_ISSUER_IDENTIFICATION_NUMBER)
				.effectiveDate(10000000L)
				.build(),
			ServiceSupplier.builder()
				.issuerIdentificationNumber("qux")
				.effectiveDate(10000000L)
				.build(),
			ServiceSupplier.builder()
				.kind(SupplierKind.OTHER)
				.issuerIdentificationNumber("foobar")
				.build()
		);

		// hydrate UUIDs
		serviceSuppliers.forEach(ss -> ss.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ss.getIssuerIdentificationNumber())));
		return serviceSuppliers;
	}
}
