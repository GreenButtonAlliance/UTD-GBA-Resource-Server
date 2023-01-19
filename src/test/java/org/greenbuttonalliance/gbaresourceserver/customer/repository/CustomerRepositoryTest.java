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
import org.greenbuttonalliance.gbaresourceserver.customer.model.Customer;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Organisation;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Priority;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Status;
import org.greenbuttonalliance.gbaresourceserver.customer.model.enums.CustomerKind;
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
public class CustomerRepositoryTest {
	@Autowired
	private CustomerRepository customerRepository;

	// for testing findById
	private static final String PRESENT_PUC = "foo";
	private static final String NOT_PRESENT_PUC = "bar";

	@BeforeEach
	public void initTestData() {
		customerRepository.deleteAllInBatch();
		customerRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_PUC);
		UUID foundUuid = customerRepository.findById(presentUuid).map(Customer::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_PUC);
		Optional<Customer> customer = customerRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			customer.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, customer.map(Customer::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = customerRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		Customer fullyMappedCustomer = customerRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_PUC)).orElse(null);
		Assumptions.assumeTrue(fullyMappedCustomer != null);

		Function<Customer, Optional<Organisation>> customerToOrganisation = c -> Optional.ofNullable(c.getOrganisation());
		Function<Customer, Optional<Status>> customerToStatus = c -> Optional.ofNullable(c.getStatus());
		Function<Customer, Optional<Priority>> customerToPriority = c -> Optional.ofNullable(c.getPriority());

		Assertions.assertAll(
			"Entity mapping failures for customer " + fullyMappedCustomer.getUuid(),
			Stream.of(customerToOrganisation,
					customerToStatus,
					customerToPriority)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedCustomer).isPresent()))
		);
	}

	private static List<Customer> buildTestData() {
		List<Customer> customers = Arrays.asList(
			Customer.builder()
				.organisation(Organisation.builder()
					.organisationName("foo")
					.build())
				.kind(CustomerKind.RESIDENTIAL)
				.specialNeed("bar")
				.vip(true)
				.pucNumber(PRESENT_PUC)
				.status(Status.builder()
					.value("bazbar")
					.build())
				.priority(Priority.builder()
					.rank(1L)
					.type("foobar")
					.justification("foobazbar")
					.build())
				.locale("qux")
				.customerName("bazqux")
				.build(),
			Customer.builder()
				.kind(CustomerKind.RESIDENTIAL)
				.pucNumber("baz")
				.locale("qux")
				.customerName("bazqux")
				.build(),
			Customer.builder()
				.organisation(Organisation.builder()
					.organisationName("bar")
					.build())
				.specialNeed("foobar")
				.vip(true)
				.pucNumber("fooqux")
				.build()
		);

		// hydrate UUIDs
		customers.forEach(c -> c.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, c.getPucNumber())));
		return customers;
	}
}
