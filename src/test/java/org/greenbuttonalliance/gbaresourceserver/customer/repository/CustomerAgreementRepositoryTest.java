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

package org.greenbuttonalliance.gbaresourceserver.customer.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.customer.model.CustomerAgreement;
import org.greenbuttonalliance.gbaresourceserver.customer.model.DemandResponseProgram;
import org.greenbuttonalliance.gbaresourceserver.customer.model.ProgramDate;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Status;
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
public class CustomerAgreementRepositoryTest {
	@Autowired
	private CustomerAgreementRepository customerAgreementRepository;

	// for testing findById
	private static final String PRESENT_AGREEMENT_ID = "foo";
	private static final String NOT_PRESENT_AGREEMENT_ID = "bar";

	@BeforeEach
	public void initTestData() {
		customerAgreementRepository.deleteAllInBatch();
		customerAgreementRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_AGREEMENT_ID);
		UUID foundUuid = customerAgreementRepository.findById(presentUuid).map(CustomerAgreement::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_AGREEMENT_ID);
		Optional<CustomerAgreement> customerAgreement = customerAgreementRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			customerAgreement.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, customerAgreement.map(CustomerAgreement::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = customerAgreementRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		CustomerAgreement fullyMappedCustomerAgreement = customerAgreementRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_AGREEMENT_ID)).orElse(null);
		Assumptions.assumeTrue(fullyMappedCustomerAgreement != null);

		// first-level mappings; directly from fullyMappedCustomerAgreement
		Function<CustomerAgreement, Optional<Set<DemandResponseProgram>>> customerAgreementToDemandResponsePrograms = ca -> Optional.ofNullable(ca.getDemandResponsePrograms());
		Function<CustomerAgreement, Optional<Set<String>>> customerAgreementToPricingStructures = ca -> Optional.ofNullable(ca.getPricingStructures());
		Function<CustomerAgreement, Optional<Set<Status>>> customerAgreementToFutureStatuses = ca -> Optional.ofNullable(ca.getFutureStatuses());

		// second-level mappings; directly from fullyMappedCustomerAgreement.demandResponsePrograms.stream().findFirst()
		Function<CustomerAgreement, Optional<Set<ProgramDate>>> customerAgreementToProgramDates = customerAgreementToDemandResponsePrograms.andThen(opt -> opt.flatMap(
			demandResponsePrograms -> demandResponsePrograms.stream().findFirst().map(DemandResponseProgram::getProgramDates)
		));
		Function<CustomerAgreement, Optional<SummaryMeasurement>> customerAgreementToCapacityReservationLevel = customerAgreementToDemandResponsePrograms.andThen(opt -> opt.flatMap(
			demandResponsePrograms -> demandResponsePrograms.stream().findFirst().map(DemandResponseProgram::getCapacityReservationLevel)
		));
		Function<CustomerAgreement, Optional<SummaryMeasurement>> customerAgreementToDrProgramNomination = customerAgreementToDemandResponsePrograms.andThen(opt -> opt.flatMap(
			demandResponsePrograms -> demandResponsePrograms.stream().findFirst().map(DemandResponseProgram::getDrProgramNomination)
		));

		Assertions.assertAll(
			"Entity mapping failures for customer agreement " + fullyMappedCustomerAgreement.getUuid(),
			Stream.of(customerAgreementToDemandResponsePrograms,
					customerAgreementToPricingStructures,
					customerAgreementToFutureStatuses,
					customerAgreementToProgramDates,
					customerAgreementToCapacityReservationLevel,
					customerAgreementToDrProgramNomination)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedCustomerAgreement).isPresent()))
		);
	}

	private static List<CustomerAgreement> buildTestData() {
		List<CustomerAgreement> customerAgreements = Arrays.asList(
			CustomerAgreement.builder()
				.signDate(4000000L)
				.validityInterval(new DateTimeInterval()
					.setStart(1330578800L)
					.setDuration(900L))
				.loadMgmt("foo")
				.isPrePay(false)
				.shutOffDateTime(50000000L)
				.demandResponsePrograms(Stream.of(
						DemandResponseProgram.builder()
							.programName("bar")
							.enrollmentStatus(EnrollmentStatus.ENROLLED)
							.programDescription("baz")
							.programDates(Stream.of(
								ProgramDate.builder()
									.programDate(12345678L)
									.programDateDescription("foobar")
									.build(),
								ProgramDate.builder()
									.programDate(99999999L)
									.programDateDescription("bazbar")
									.build())
								.collect(Collectors.toSet()))
							.capacityReservationLevel(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.W_H)
								.setValue(2L)
								.setReadingTypeRef("readingTypeRef"))
							.drProgramNomination(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.PICO)
								.setTimeStamp(3L)
								.setUom(UnitSymbolKind.FT3_COMPENSATED_PER_H)
								.setValue(4L)
								.setReadingTypeRef("readingTypeRef"))
							.build(),
						DemandResponseProgram.builder()
							.programName("qux")
							.enrollmentStatus(EnrollmentStatus.ENROLLED_PENDING)
							.programDescription("foobazbar")
							.programDates(Stream.of(
									ProgramDate.builder()
										.programDate(10000000L)
										.programDateDescription("fooqux")
										.build())
								.collect(Collectors.toSet()))
							.capacityReservationLevel(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.GIGA)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.drProgramNomination(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.HECTO)
								.setTimeStamp(5L)
								.setUom(UnitSymbolKind.W_H_PER_REV)
								.setValue(6L)
								.setReadingTypeRef("readingTypeRef"))
							.build())
					.collect(Collectors.toSet()))
				.pricingStructures(
					Stream.of("asdf",
							"jkl;")
						.collect(Collectors.toSet()))
				.currency(Currency.USD)
				.futureStatuses(Stream.of(
					Status.builder()
						.value("qwerty")
						.build(),
					Status.builder()
						.value("alpha")
						.build())
					.collect(Collectors.toSet()))
				.docStatus(Status.builder()
					.value("baz")
					.build())
				.status(Status.builder()
					.value("qux")
					.build())
				.agreementId(PRESENT_AGREEMENT_ID)
				.build(),
			CustomerAgreement.builder()
				.validityInterval(new DateTimeInterval()
					.setDuration(800L))
				.demandResponsePrograms(Stream.of(
						DemandResponseProgram.builder()
							.enrollmentStatus(EnrollmentStatus.ENROLLED)
							.programDates(Stream.of(
									ProgramDate.builder()
										.programDate(99999999L)
										.programDateDescription("bazbar")
										.build())
								.collect(Collectors.toSet()))
							.drProgramNomination(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.PICO)
								.setTimeStamp(3L)
								.setUom(UnitSymbolKind.FT3_COMPENSATED_PER_H)
								.setValue(4L)
								.setReadingTypeRef("readingTypeRef"))
							.build(),
						DemandResponseProgram.builder()
							.programName("qux")
							.enrollmentStatus(EnrollmentStatus.ENROLLED_PENDING)
							.programDescription("foobazbar")
							.programDates(Stream.of(
									ProgramDate.builder()
										.programDate(11111111L)
										.programDateDescription("quxfoo")
										.build())
								.collect(Collectors.toSet()))
							.drProgramNomination(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.build())
					.collect(Collectors.toSet()))
				.pricingStructures(
					Stream.of("beta",
							"gamma")
						.collect(Collectors.toSet()))
				.currency(Currency.AUD)
				.futureStatuses(Stream.of(
						Status.builder()
							.value("delta")
							.build())
					.collect(Collectors.toSet()))
				.docStatus(Status.builder()
					.value("epsilon")
					.build())
				.agreementId("phi")
				.build(),
			CustomerAgreement.builder()
				.agreementId("omicron")
				.build()
		);

		// hydrate UUIDs
		customerAgreements.forEach(ca -> ca.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ca.getAgreementId())));
		return customerAgreements;
	}
}
