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

package org.greenbuttonalliance.gbaresourceserver.customer.integration;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.customer.model.AccountNotification;
import org.greenbuttonalliance.gbaresourceserver.customer.model.CustomerAccount;
import org.greenbuttonalliance.gbaresourceserver.customer.model.ElectronicAddress;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Organisation;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Status;
import org.greenbuttonalliance.gbaresourceserver.customer.model.StreetAddress;
import org.greenbuttonalliance.gbaresourceserver.customer.model.StreetDetail;
import org.greenbuttonalliance.gbaresourceserver.customer.model.TelephoneNumber;
import org.greenbuttonalliance.gbaresourceserver.customer.model.TownDetail;
import org.greenbuttonalliance.gbaresourceserver.customer.model.enums.NotificationMethodKind;
import org.greenbuttonalliance.gbaresourceserver.customer.repository.CustomerAccountRepository;
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
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerAccountRepositoryITest {
	@Autowired
	private CustomerAccountRepository customerAccountRepository;

	// for testing findById
	private static final String PRESENT_TITLE = "foo";
	private static final String NOT_PRESENT_TITLE = "bar";

	@BeforeEach
	public void initTestData() {
		customerAccountRepository.deleteAllInBatch();
		customerAccountRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_TITLE);
		UUID foundUuid = customerAccountRepository.findById(presentUuid).map(CustomerAccount::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_TITLE);
		Optional<CustomerAccount> customerAccount = customerAccountRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			customerAccount.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, customerAccount.map(CustomerAccount::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = customerAccountRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		CustomerAccount fullyMappedCustomerAccount = customerAccountRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_TITLE)).orElse(null);
		Assumptions.assumeTrue(fullyMappedCustomerAccount != null);

		// first-level mappings; directly from fullyMappedCustomerAccount
		Function<CustomerAccount, Optional<ElectronicAddress>> customerAccountToElectronicAddress = ca -> Optional.ofNullable(ca.getElectronicAddress());
		Function<CustomerAccount, Optional<Status>> customerAccountToDocStatus = ca -> Optional.ofNullable(ca.getDocStatus());
		Function<CustomerAccount, Optional<Status>> customerAccountToStatus = ca -> Optional.ofNullable(ca.getStatus());
		Function<CustomerAccount, Optional<Organisation>> customerAccountToContactInfo = ca -> Optional.ofNullable(ca.getContactInfo());
		Function<CustomerAccount, Optional<Set<AccountNotification>>> customerAccountToAccountNotifications = ca -> Optional.ofNullable(ca.getAccountNotifications());

		// second-level mappings; directly from fullyMappedCustomerAccount.contactInfo
		Function<CustomerAccount, Optional<StreetAddress>> customerAccountToOrganisationStreetAddress = customerAccountToContactInfo.andThen(opt -> opt.map(
			Organisation::getStreetAddress
		));
		Function<CustomerAccount, Optional<StreetAddress>> customerAccountToOrganisationPostalAddress = customerAccountToContactInfo.andThen(opt -> opt.map(
			Organisation::getPostalAddress
		));
		Function<CustomerAccount, Optional<TelephoneNumber>> customerAccountToOrganisationPhone1 = customerAccountToContactInfo.andThen(opt -> opt.map(
			Organisation::getPhone1
		));
		Function<CustomerAccount, Optional<TelephoneNumber>> customerAccountToOrganisationPhone2 = customerAccountToContactInfo.andThen(opt -> opt.map(
			Organisation::getPhone2
		));
		Function<CustomerAccount, Optional<ElectronicAddress>> customerAccountToOrganisationElectronicAddress = customerAccountToContactInfo.andThen(opt -> opt.map(
			Organisation::getElectronicAddress
		));

		// third-level mappings; directly from fullyMappedCustomerAccount.contactInfo.streetAddress
		Function<CustomerAccount, Optional<StreetDetail>> customerAccountToOrganisationStreetAddressStreetDetail = customerAccountToOrganisationStreetAddress.andThen(opt -> opt.map(
			StreetAddress::getStreetDetail
		));
		Function<CustomerAccount, Optional<TownDetail>> customerAccountToOrganisationStreetAddressTownDetail = customerAccountToOrganisationStreetAddress.andThen(opt -> opt.map(
			StreetAddress::getTownDetail
		));
		Function<CustomerAccount, Optional<Status>> customerAccountToOrganisationStreetAddressStatus = customerAccountToOrganisationStreetAddress.andThen(opt -> opt.map(
			StreetAddress::getStatus
		));

		Assertions.assertAll(
			"Entity mapping failures for customer account " + fullyMappedCustomerAccount.getUuid(),
			Stream.of(customerAccountToElectronicAddress,
					customerAccountToDocStatus,
					customerAccountToStatus,
					customerAccountToContactInfo,
					customerAccountToAccountNotifications,
					customerAccountToOrganisationStreetAddress,
					customerAccountToOrganisationPostalAddress,
					customerAccountToOrganisationPhone1,
					customerAccountToOrganisationPhone2,
					customerAccountToOrganisationElectronicAddress,
					customerAccountToOrganisationStreetAddressStreetDetail,
					customerAccountToOrganisationStreetAddressTownDetail,
					customerAccountToOrganisationStreetAddressStatus)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedCustomerAccount).isPresent()))
		);
	}

	private static List<CustomerAccount> buildTestData() {
		List<CustomerAccount> customerAccounts = Arrays.asList(
			CustomerAccount.builder()
				.type("foo")
				.createdDateTime(20000000L)
				.revisionNumber("bar")
				.electronicAddress(ElectronicAddress.builder()
					.lan("foobar")
					.build())
				.docStatus(Status.builder()
					.value("baz")
					.build())
				.status(Status.builder()
					.value("qux")
					.build())
				.title(PRESENT_TITLE)
				.budgetBill("foobazbar")
				.contactInfo(Organisation.builder()
					.streetAddress(StreetAddress.builder()
						.streetDetail(StreetDetail.builder()
							.number("fooqux")
							.withinTownLimits(false)
							.build())
						.townDetail(TownDetail.builder()
							.section("bazbar")
							.country("barbaz")
							.build())
						.status(Status.builder()
							.dateTime(500L)
							.build())
						.poBox("foo")
						.build())
					.postalAddress(StreetAddress.builder()
						.streetDetail(StreetDetail.builder()
							.number("fooqux")
							.withinTownLimits(false)
							.build())
						.townDetail(TownDetail.builder()
							.section("bazbar")
							.country("barbaz")
							.build())
						.status(Status.builder()
							.dateTime(500L)
							.build())
						.poBox("foo")
						.build())
					.phone1(TelephoneNumber.builder()
						.areaCode("baz")
						.build())
					.phone2(TelephoneNumber.builder()
						.cityCode("foobar")
						.build())
					.electronicAddress(ElectronicAddress.builder()
						.radio("qux")
						.build())
					.organisationName("bazbar")
					.build())
				.accountNotifications(Stream.of(
					AccountNotification.builder()
						.methodKind(NotificationMethodKind.EMAIL)
						.build(),
					AccountNotification.builder()
						.methodKind(NotificationMethodKind.LETTER)
						.build())
					.collect(Collectors.toSet()))
				.build(),
			CustomerAccount.builder()
				.type("foo")
				.revisionNumber("bar")
				.electronicAddress(ElectronicAddress.builder()
					.lan("foobar")
					.build())
				.status(Status.builder()
					.value("qux")
					.build())
				.title("qux")
				.budgetBill("foobazbar")
				.contactInfo(Organisation.builder()
					.postalAddress(StreetAddress.builder()
						.streetDetail(StreetDetail.builder()
							.number("fooqux")
							.withinTownLimits(false)
							.build())
						.status(Status.builder()
							.dateTime(500L)
							.build())
						.poBox("foo")
						.build())
					.phone1(TelephoneNumber.builder()
						.areaCode("baz")
						.build())
					.electronicAddress(ElectronicAddress.builder()
						.radio("qux")
						.build())
					.organisationName("bazbar")
					.build())
				.accountNotifications(Stream.of(
						AccountNotification.builder()
							.methodKind(NotificationMethodKind.EMAIL)
							.build())
					.collect(Collectors.toSet()))
				.build(),
			CustomerAccount.builder()
				.revisionNumber("bar")
				.electronicAddress(ElectronicAddress.builder()
					.lan("foobar")
					.build())
				.docStatus(Status.builder()
					.value("baz")
					.build())
				.title("bazbar")
				.budgetBill("foobazbar")
				.contactInfo(Organisation.builder()
					.streetAddress(StreetAddress.builder()
						.streetDetail(StreetDetail.builder()
							.number("fooqux")
							.withinTownLimits(false)
							.build())
						.townDetail(TownDetail.builder()
							.section("bazbar")
							.country("barbaz")
							.build())
						.poBox("foo")
						.build())
					.phone2(TelephoneNumber.builder()
						.cityCode("foobar")
						.build())
					.electronicAddress(ElectronicAddress.builder()
						.radio("qux")
						.build())
					.organisationName("bazbar")
					.build())
				.build()
		);

		// hydrate UUIDs
		customerAccounts.forEach(ca -> ca.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ca.getTitle())));
		return customerAccounts;
	}
}
