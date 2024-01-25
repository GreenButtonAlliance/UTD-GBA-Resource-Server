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
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.*;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsagePointRepositoryTest {
	private final UsagePointRepository usagePointRepository;

	// for testing findById
	private static final String upLinkHref = "https://data.greenbuttonconnect.org" +
											 "/DataCustodian/espi/1_1/resource/UsagePoint";
	private static final String PRESENT_SELF_LINK = "https://data.greenbuttonconnect.org" +
													"/DataCustodian/espi/1_1/resource" +
													"/UsagePoint/123456";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		usagePointRepository.deleteAllInBatch();
		usagePointRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = usagePointRepository.findById(presentUuid).map(UsagePoint::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<UsagePoint> usagePoint = usagePointRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			usagePoint.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, usagePoint.map(UsagePoint::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = usagePointRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		UsagePoint fullyMappedUsagePoint = usagePointRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedUsagePoint != null);

		Function<UsagePoint, Optional<ServiceDeliveryPoint>> usagePointToServiceDeliveryPoint = up -> Optional.ofNullable(up.getServiceDeliveryPoint());

		Function<UsagePoint, Optional<TimeConfiguration>> usagePointToTimeConfiguration = up -> Optional.ofNullable(up.getTimeConfiguration());

		Function<UsagePoint, Optional<RetailCustomer>> usagePointToRetailCustomer = up -> Optional.ofNullable(up.getRetailCustomer());

		Function<UsagePoint, Optional<Set<MeterReading>>> usagePointToMeterReadings = up -> Optional.ofNullable(up.getMeterReadings());

		Function<UsagePoint, Optional<Set<ElectricPowerQualitySummary>>> usagePointToElectricPowerQualitySummaries = up -> Optional.ofNullable(up.getElectricPowerQualitySummaries());

		Function<UsagePoint, Optional<Set<UsageSummary>>> usagePointToUsageSummaries = up -> Optional.ofNullable(up.getUsageSummaries());

		Function<UsagePoint, Optional<Set<PnodeRef>>> usagePointToPnodeRefs = up -> Optional.ofNullable(up.getPnodeRefs());

		Function<UsagePoint, Optional<Set<AggregateNodeRef>>> usagePointToAggregateNodeRefs = up -> Optional.ofNullable(up.getAggregateNodeRefs());

		Assertions.assertAll(
			"Entity mapping failures for usage point " + fullyMappedUsagePoint.getUuid(),
			Stream.of(usagePointToServiceDeliveryPoint,
					usagePointToTimeConfiguration,
					usagePointToRetailCustomer,
					usagePointToMeterReadings,
					usagePointToElectricPowerQualitySummaries,
					usagePointToUsageSummaries,
					usagePointToPnodeRefs,
					usagePointToAggregateNodeRefs)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedUsagePoint).isPresent()))
		);
	}

	private static List<UsagePoint> buildTestData() {
		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();
		List<UsagePoint> usagePoints = Arrays.asList(
			UsagePoint.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.roleFlags(new byte[1])
				.serviceCategory(ServiceKind.ELECTRICITY)
				.status((short) 1)
				.serviceDeliveryPoint(ServiceDeliveryPoint.builder()
					.name("name")
					.tariffProfile("tariffProfile")
					.customerAgreement("customerAgreement")
					.tariffRiderRefs(
						new HashSet<>(
							Collections.singletonList(
								TariffRiderRef.builder()
									.enrollmentStatus(EnrollmentStatus.ENROLLED)
									.effectiveDate(1L)
									.riderType("riderType")
									.build()
							)
						)
					)
					.build())
				.timeConfiguration(TimeConfiguration.builder()
					.published(LocalDateTime.parse("2014-11-18 12:20:45", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/184")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
					.updated(LocalDateTime.parse("2015-10-15 12:21:30", SQL_FORMATTER))
					.dstEndRule(deadbeefs)
					.dstOffset(200L)
					.dstStartRule(deadbeefs)
					.tzOffset(20L)
					.build())
				.retailCustomer(RetailCustomer.builder()
					.description("Hourly Wh Received")
					.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/1")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.enabled(Boolean.FALSE)
					.firstName("First")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("aUsername")
					.build())
				.meterReadings(new HashSet<>(
					Collections.singletonList(
						MeterReading.builder()
							.description("Fifteen Minute Electricity Consumption")
							.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
							.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
							.readingType(ReadingType.builder()
								.description("Type of Meter Reading Data")
								.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
								.selfLinkHref(PRESENT_SELF_LINK)
								.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
								.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
								.accumulationBehavior(AccumulationKind.DELTA_DATA)
								.commodity(CommodityKind.ELECTRICITY_SECONDARY_METERED)
								.consumptionTier(null)
								.currency(Currency.USD)
								.dataQualifier(DataQualifierKind.NORMAL)
								.defaultQuality(null)
								.flowDirection(FlowDirectionKind.FORWARD)
								.intervalLength(900L)
								.kind(MeasurementKind.ENERGY)
								.phase(PhaseCodeKind.S12N)
								.powerOfTenMultiplier(UnitMultiplierKind.NONE)
								.timeAttribute(TimeAttributeKind.NONE)
								.tou(null)
								.uom(UnitSymbolKind.W_H)
								.cpp(null)
								.interharmonicNumerator(600L)
								.interharmonicDenominator(800L)
								.measuringPeriod(null)
								.argumentNumerator(1L)
								.argumentDenominator(2L)
								.build())
							.intervalBlocks(Stream.of(
									IntervalBlock.builder()
										.description("UsagePoint Repository Test IntervalBlock 1")
										.published(LocalDateTime.now())
										.selfLinkHref("https://data.greenbuttonconnect" +
													  ".org/DataCustodian/espi/1_1/resource" +
													  "/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173")
										.upLinkHref("https://data.greenbuttonconnect" +
													".org/DataCustodian/espi/1_1/resource" +
													"/RetailCustomer" +
													"/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
										.updated(LocalDateTime.now())
										.interval(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build(),
									IntervalBlock.builder()
										.description("UsagePoint Repository Test IntervalBlock 2")
										.published(LocalDateTime.now())
										.selfLinkHref("https://data.greenbuttonconnect" +
													  ".org/DataCustodian/espi/1_1/resource" +
													  "/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174")
										.upLinkHref("https://data.greenbuttonconnect" +
													".org/DataCustodian/espi/1_1/resource" +
													"/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
										.updated(LocalDateTime.now())
										.interval(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build())
								.collect(Collectors.toSet()))
							.build()
					)
				))
				.electricPowerQualitySummaries(new HashSet<>(
					Collections.singletonList(
						ElectricPowerQualitySummary.builder()
							.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref(upLinkHref)
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
							.build()
					)
				))
				.usageSummaries(new HashSet<>(
					Collections.singletonList(
						UsageSummary.builder()
							.description("description")
							.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref(upLinkHref)
							.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.billingPeriod(new DateTimeInterval()
								.setDuration(10L)
								.setStart(11L))
							.billLastPeriod(1L)
							.billToDate(1L)
							.costAdditionalLastPeriod(1L)
							.lineItems(new HashSet<>(List.of(
								LineItem.builder()
									.amount(1L)
									.rounding(1L)
									.dateTime(1L)
									.note("note")
									.measurement(new SummaryMeasurement()
										.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
										.setTimeStamp(1L)
										.setUom(UnitSymbolKind.M)
										.setValue(1L)
										.setReadingTypeRef("readingTypeRef"))
									.itemKind(ItemKind.INFORMATION)
									.unitCost(1L)
									.itemPeriod(new DateTimeInterval()
										.setDuration(10L)
										.setStart(11L))
									.id(100001L).build()
								)
								)
							)
							.currency(Currency.USD)
							.overallConsumptionLastPeriod(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayLastYearNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.peakDemand(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayLastYearOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.qualityOfReading(QualityOfReading.VALID)
							.ratchetDemand(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.ratchetDemandPeriod(new DateTimeInterval()
								.setDuration(10L)
								.setStart(11L))
							.statusTimeStamp(1L)
							.commodity(CommodityKind.CO2)
							.tariffProfile("tariffProfile")
							.readCycle("readCycle")
							.tariffRiderRefs(
								new HashSet<>(
									Collections.singletonList(
										TariffRiderRef.builder()
											.enrollmentStatus(EnrollmentStatus.ENROLLED)
											.effectiveDate(1L)
											.riderType("riderType")
											.build()
									)
								)
							)
							.billingChargeSource(new BillingChargeSource()
								.setAgencyName("agencyName"))
							.build()
					)
				))
				.amiBillingReady(AmiBillingReadyKind.OPERABLE)
				.checkBilling(true)
				.connectionState(UsagePointConnectedKind.CONNECTED)
				.estimatedLoad(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.grounded(true)
				.isSdp(true)
				.isVirtual(true)
				.minimalUsageExpected(true)
				.nominalServiceVoltage(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.outageRegion("outageRegion")
				.phaseCode(PhaseCodeKind.S12N)
				.ratedCurrent(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.ratedPower(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.readCycle("readCycle")
				.readRoute("readRoute")
				.serviceDeliveryRemark("serviceDeliveryRemark")
				.servicePriority("servicePriority")
				.pnodeRefs(new HashSet<>(
					Collections.singletonList(
						PnodeRef.builder()
							.id(1L)
							.apnodeType(ApnodeType.AG)
							.ref("Pnode_1")
							.build()
					)
				))
				.aggregateNodeRefs(new HashSet<>(
					Collections.singletonList(
						AggregateNodeRef.builder()
							.anodeType(AnodeType.AGR)
							.ref("Anode_1")
							.build()
					)
				))
				.build(),

			UsagePoint.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/UsagePoint/175")
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.roleFlags(new byte[1])
				.serviceCategory(ServiceKind.WATER)
				.status((short) 1)
				.serviceDeliveryPoint(ServiceDeliveryPoint.builder()
					.name("name")
					.tariffProfile("tariffProfile")
					.customerAgreement("customerAgreement")
					.tariffRiderRefs(
						new HashSet<>(
							Collections.singletonList(
								TariffRiderRef.builder()
									.enrollmentStatus(EnrollmentStatus.ENROLLED)
									.effectiveDate(1L)
									.riderType("riderType")
									.build()
							)
						)
					)
					.build())
				.timeConfiguration(TimeConfiguration.builder()
					.published(LocalDateTime.parse("2014-11-18 12:20:45", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/185")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
					.updated(LocalDateTime.parse("2015-10-15 12:21:30", SQL_FORMATTER))
					.dstEndRule(deadbeefs)
					.dstOffset(200L)
					.dstStartRule(deadbeefs)
					.tzOffset(20L)
					.build())
				.retailCustomer(RetailCustomer.builder()
					.description("Hourly Wh Received")
					.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/1")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.enabled(Boolean.FALSE)
					.firstName("First")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("aUsername")
					.build())
				.meterReadings(new HashSet<>(
					Collections.singletonList(
						MeterReading.builder()
							.description("Fifteen Minute Electricity Consumption")
							.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
							.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
							.readingType(ReadingType.builder()
								.description("Type of Meter Reading Data")
								.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
								.selfLinkHref(PRESENT_SELF_LINK)
								.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
								.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
								.accumulationBehavior(AccumulationKind.DELTA_DATA)
								.commodity(CommodityKind.ELECTRICITY_SECONDARY_METERED)
								.consumptionTier(null)
								.currency(Currency.USD)
								.dataQualifier(DataQualifierKind.NORMAL)
								.defaultQuality(null)
								.flowDirection(FlowDirectionKind.FORWARD)
								.intervalLength(900L)
								.kind(MeasurementKind.ENERGY)
								.phase(PhaseCodeKind.S12N)
								.powerOfTenMultiplier(UnitMultiplierKind.NONE)
								.timeAttribute(TimeAttributeKind.NONE)
								.tou(null)
								.uom(UnitSymbolKind.W_H)
								.cpp(null)
								.interharmonicNumerator(600L)
								.interharmonicDenominator(800L)
								.measuringPeriod(null)
								.argumentNumerator(1L)
								.argumentDenominator(2L)
								.build())
							.intervalBlocks(Stream.of(
									IntervalBlock.builder()
										.published(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
										.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173")
										.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
										.updated(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
										.interval(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build(),
									IntervalBlock.builder()
										.published(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
										.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174")
										.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
										.updated(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
										.interval(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build())
								.collect(Collectors.toSet()))
							.build()
					)
				))
				.electricPowerQualitySummaries(new HashSet<>(
					Collections.singletonList(
						ElectricPowerQualitySummary.builder()
							.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref(upLinkHref)
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
							.build()
					)
				))
				.usageSummaries(new HashSet<>(
					Collections.singletonList(
						UsageSummary.builder()
							.description("description")
							.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref(upLinkHref)
							.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.billingPeriod(new DateTimeInterval()
								.setDuration(10L)
								.setStart(11L))
							.billLastPeriod(1L)
							.billToDate(1L)
							.costAdditionalLastPeriod(1L)
							.lineItems(new HashSet<>(List.of(
									LineItem.builder()
										.amount(1L)
										.rounding(1L)
										.dateTime(1L)
										.note("note")
										.measurement(new SummaryMeasurement()
											.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
											.setTimeStamp(1L)
											.setUom(UnitSymbolKind.M)
											.setValue(1L)
											.setReadingTypeRef("readingTypeRef"))
										.itemKind(ItemKind.INFORMATION)
										.unitCost(1L)
										.itemPeriod(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build()
								)
								)
							)
							.currency(Currency.USD)
							.overallConsumptionLastPeriod(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayLastYearNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.peakDemand(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayLastYearOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.qualityOfReading(QualityOfReading.VALID)
							.ratchetDemand(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.ratchetDemandPeriod(new DateTimeInterval()
								.setDuration(10L)
								.setStart(11L))
							.statusTimeStamp(1L)
							.commodity(CommodityKind.CO2)
							.tariffProfile("tariffProfile")
							.readCycle("readCycle")
							.tariffRiderRefs(
								new HashSet<>(
									Collections.singletonList(
										TariffRiderRef.builder()
											.enrollmentStatus(EnrollmentStatus.ENROLLED)
											.effectiveDate(1L)
											.riderType("riderType")
											.build()
									)
								)
							)
							.billingChargeSource(new BillingChargeSource()
								.setAgencyName("agencyName"))
							.build()
					)
				))
				.amiBillingReady(AmiBillingReadyKind.OPERABLE)
				.checkBilling(true)
				.connectionState(UsagePointConnectedKind.CONNECTED)
				.estimatedLoad(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.grounded(true)
				.isSdp(true)
				.isVirtual(true)
				.minimalUsageExpected(true)
				.nominalServiceVoltage(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.outageRegion("outageRegion")
				.phaseCode(PhaseCodeKind.S12N)
				.ratedCurrent(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.ratedPower(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.readCycle("readCycle")
				.readRoute("readRoute")
				.serviceDeliveryRemark("serviceDeliveryRemark")
				.servicePriority("servicePriority")
				.pnodeRefs(new HashSet<>(
					Collections.singletonList(
						PnodeRef.builder()
							.id(2L)
							.apnodeType(ApnodeType.AG)
							.ref("Pnode_2")
							.build()
					)
				))
				.aggregateNodeRefs(new HashSet<>(
					Collections.singletonList(
						AggregateNodeRef.builder()
							.anodeType(AnodeType.AGR)
							.ref("Anode_2")
							.build()
					)
				))
				.build(),

			UsagePoint.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/UsagePoint/176")
				.upLinkHref(upLinkHref)
				.updated(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.roleFlags(new byte[1])
				.serviceCategory(ServiceKind.ELECTRICITY)
				.status((short) 1)
				.serviceDeliveryPoint(ServiceDeliveryPoint.builder()
					.name("name")
					.tariffProfile("tariffProfile")
					.customerAgreement("customerAgreement")
					.tariffRiderRefs(
						new HashSet<>(
							Collections.singletonList(
								TariffRiderRef.builder()
									.enrollmentStatus(EnrollmentStatus.ENROLLED)
									.effectiveDate(1L)
									.riderType("riderType")
									.build()
							)
						)
					)
					.build())
				.timeConfiguration(TimeConfiguration.builder()
					.published(LocalDateTime.parse("2014-11-18 12:20:45", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/186")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
					.updated(LocalDateTime.parse("2015-10-15 12:21:30", SQL_FORMATTER))
					.dstEndRule(deadbeefs)
					.dstOffset(200L)
					.dstStartRule(deadbeefs)
					.tzOffset(20L)
					.build())
				.retailCustomer(RetailCustomer.builder()
					.description("Hourly Wh Received")
					.published(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/1")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.enabled(Boolean.FALSE)
					.firstName("First")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("aUsername")
					.build())
				.meterReadings(new HashSet<>(
					Collections.singletonList(
						MeterReading.builder()
							.description("Fifteen Minute Electricity Consumption")
							.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01")
							.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
							.readingType(ReadingType.builder()
								.description("Type of Meter Reading Data")
								.published(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
								.selfLinkHref(PRESENT_SELF_LINK)
								.upLinkHref("https://{domain}/espi/1_1/resource/ReadingType")
								.updated(LocalDateTime.parse("2012-10-24 04:00:00", SQL_FORMATTER))
								.accumulationBehavior(AccumulationKind.DELTA_DATA)
								.commodity(CommodityKind.ELECTRICITY_SECONDARY_METERED)
								.consumptionTier(null)
								.currency(Currency.USD)
								.dataQualifier(DataQualifierKind.NORMAL)
								.defaultQuality(null)
								.flowDirection(FlowDirectionKind.FORWARD)
								.intervalLength(900L)
								.kind(MeasurementKind.ENERGY)
								.phase(PhaseCodeKind.S12N)
								.powerOfTenMultiplier(UnitMultiplierKind.NONE)
								.timeAttribute(TimeAttributeKind.NONE)
								.tou(null)
								.uom(UnitSymbolKind.W_H)
								.cpp(null)
								.interharmonicNumerator(600L)
								.interharmonicDenominator(800L)
								.measuringPeriod(null)
								.argumentNumerator(1L)
								.argumentDenominator(2L)
								.build())
							.intervalBlocks(Stream.of(
									IntervalBlock.builder()
										.published(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
										.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173")
										.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
										.updated(LocalDateTime.parse("2012-03-02 05:00:00", SQL_FORMATTER))
										.interval(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build(),
									IntervalBlock.builder()
										.published(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
										.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174")
										.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock")
										.updated(LocalDateTime.parse("2012-03-03 05:00:00", SQL_FORMATTER))
										.interval(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build())
								.collect(Collectors.toSet()))
							.build()
					)
				))
				.electricPowerQualitySummaries(new HashSet<>(
					Collections.singletonList(
						ElectricPowerQualitySummary.builder()
							.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref(upLinkHref)
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
							.build()
					)
				))
				.usageSummaries(new HashSet<>(
					Collections.singletonList(
						UsageSummary.builder()
							.description("description")
							.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.selfLinkHref(PRESENT_SELF_LINK)
							.upLinkHref(upLinkHref)
							.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
							.billingPeriod(new DateTimeInterval()
								.setDuration(10L)
								.setStart(11L))
							.billLastPeriod(1L)
							.billToDate(1L)
							.costAdditionalLastPeriod(1L)
							.lineItems(new HashSet<>(List.of(
									LineItem.builder()
										.amount(1L)
										.rounding(1L)
										.dateTime(1L)
										.note("note")
										.measurement(new SummaryMeasurement()
											.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
											.setTimeStamp(1L)
											.setUom(UnitSymbolKind.M)
											.setValue(1L)
											.setReadingTypeRef("readingTypeRef"))
										.itemKind(ItemKind.INFORMATION)
										.unitCost(1L)
										.itemPeriod(new DateTimeInterval()
											.setDuration(10L)
											.setStart(11L))
										.build()
								)
								)
							)
							.currency(Currency.USD)
							.overallConsumptionLastPeriod(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentBillingPeriodOverAllConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayLastYearNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.currentDayOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.peakDemand(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayLastYearOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayNetConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.previousDayOverallConsumption(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.qualityOfReading(QualityOfReading.VALID)
							.ratchetDemand(new SummaryMeasurement()
								.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
								.setTimeStamp(1L)
								.setUom(UnitSymbolKind.M)
								.setValue(1L)
								.setReadingTypeRef("readingTypeRef"))
							.ratchetDemandPeriod(new DateTimeInterval()
								.setDuration(10L)
								.setStart(11L))
							.statusTimeStamp(1L)
							.commodity(CommodityKind.CO2)
							.tariffProfile("tariffProfile")
							.readCycle("readCycle")
							.tariffRiderRefs(
								new HashSet<>(
									Collections.singletonList(
										TariffRiderRef.builder()
											.enrollmentStatus(EnrollmentStatus.ENROLLED)
											.effectiveDate(1L)
											.riderType("riderType")
											.build()
									)
								)
							)
							.billingChargeSource(new BillingChargeSource()
								.setAgencyName("agencyName"))
							.build()
					)
				))
				.amiBillingReady(AmiBillingReadyKind.OPERABLE)
				.checkBilling(true)
				.connectionState(UsagePointConnectedKind.CONNECTED)
				.estimatedLoad(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.grounded(true)
				.isSdp(true)
				.isVirtual(true)
				.minimalUsageExpected(true)
				.nominalServiceVoltage(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.outageRegion("outageRegion")
				.phaseCode(PhaseCodeKind.S12N)
				.ratedCurrent(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.ratedPower(new SummaryMeasurement()
					.setPowerOfTenMultiplier(UnitMultiplierKind.NONE)
					.setTimeStamp(1L)
					.setUom(UnitSymbolKind.M)
					.setValue(1L)
					.setReadingTypeRef("readingTypeRef"))
				.readCycle("readCycle")
				.readRoute("readRoute")
				.serviceDeliveryRemark("serviceDeliveryRemark")
				.servicePriority("servicePriority")
				.pnodeRefs(new HashSet<>(
					Collections.singletonList(
						PnodeRef.builder()
							.id(3L)
							.apnodeType(ApnodeType.AG)
							.ref("Pnode_3")
							.build()
					)
				))
				.aggregateNodeRefs(new HashSet<>(
					Collections.singletonList(
						AggregateNodeRef.builder()
							.anodeType(AnodeType.AGR)
							.ref("Anode_3")
							.build()
					)
				))
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		usagePoints.forEach(up -> {

			count.getAndIncrement();

			TestUtils.hydrateConnectedUsagePointEntities(up, count.toString());

			TestUtils.connectUsagePoint(up);
		});

		return usagePoints;
	}



}
