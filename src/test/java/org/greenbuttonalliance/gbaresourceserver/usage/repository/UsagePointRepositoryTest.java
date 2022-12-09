package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.common.model.AggregateNodeRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.PnodeRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.TariffRiderRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ServiceDeliveryPoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AmIBillingReadyKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.PhaseCodeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UsagePointConnectedKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsagePointRepositoryTest {
	private final UsagePointRepository usagePointRepository;

	// for testing findById

	private static final String upLinkHref = "https://{domain}/espi/1_1/resource/UsagePoint";

	private static final String PRESENT_SELF_LINK = "https://{domain}/espi/1_1/resource/UsagePoint/174";

	private static final String NOT_PRESENT_SELF_LINK = "foobar";

	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		usagePointRepository.deleteAllInBatch();
		usagePointRepository.saveAll(buildTestData());
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

		Assertions.assertAll(
			"Entity mapping failures for usage point " + fullyMappedUsagePoint.getUuid(),
			Stream.of(usagePointToServiceDeliveryPoint,
					usagePointToTimeConfiguration,
					usagePointToRetailCustomer)
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
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
				.roleFlags(new byte[1])
				//TODO:Add service category
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
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
					.upLinkRel("up")
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
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.enabled(Boolean.FALSE)
					.firstName("First")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("aUsername")
					.build())
				.amiBillingReady(AmIBillingReadyKind.OPERABLE)
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
							.build()
					)
				))
				.aggregateNodeRefs(new HashSet<>(
					Collections.singletonList(
						AggregateNodeRef.builder()
							.ref("ref")
							.build()
					)
				))
				.build(),

			UsagePoint.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/UsagePoint/175")
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2022-03-02 05:00:00", SQL_FORMATTER))
				.roleFlags(new byte[1])
				//TODO:Add service category
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
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
					.upLinkRel("up")
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
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.enabled(Boolean.FALSE)
					.firstName("First")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("aUsername")
					.build())
				.amiBillingReady(AmIBillingReadyKind.OPERABLE)
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
							.build()
					)
				))
				.aggregateNodeRefs(new HashSet<>(
					Collections.singletonList(
						AggregateNodeRef.builder()
							.ref("ref")
							.build()
					)
				))
				.build(),

			UsagePoint.builder()
				.description("description")
				.published(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.selfLinkHref("https://{domain}/espi/1_1/resource/UsagePoint/176")
				.selfLinkRel("self")
				.upLinkHref(upLinkHref)
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2022-03-03 05:00:00", SQL_FORMATTER))
				.roleFlags(new byte[1])
				//TODO:Add service category
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
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
					.upLinkRel("up")
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
					.selfLinkRel("self")
					.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
					.upLinkRel("up")
					.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
					.enabled(Boolean.FALSE)
					.firstName("First")
					.lastName("last")
					.password("password")
					.role("whatever")
					.username("aUsername")
					.build())
				.amiBillingReady(AmIBillingReadyKind.OPERABLE)
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
							.build()
					)
				))
				.aggregateNodeRefs(new HashSet<>(
					Collections.singletonList(
						AggregateNodeRef.builder()
							.ref("ref")
							.build()
					)
				))
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		usagePoints.forEach(up -> {

			count.getAndIncrement();

			hydrateConnectedUsagePointEntities(up, count.toString());

//			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
//
//			count.getAndIncrement();
//
//			ServiceDeliveryPoint sdp = up.getServiceDeliveryPoint();
//
//			sdp.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, "UUID"+count));
//
//			sdp.setUsagePoints(new HashSet<>(
//				Collections.singletonList(up)
//			));
//
//			TimeConfiguration tc = up.getTimeConfiguration();
//
//			tc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
//
//			tc.setUsagePoints(new HashSet<>(
//				Collections.singletonList(up)
//			));
		});

		return usagePoints;
	}

	public static UsagePoint createUsageRepository() {

		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();

		return UsagePoint.builder()
			.description("description")
			.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
			.selfLinkHref(PRESENT_SELF_LINK)
			.selfLinkRel("self")
			.upLinkHref(upLinkHref)
			.upLinkRel("up")
			.updated(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
			.roleFlags(new byte[1])
			.serviceCategory(ServiceKind.HEAT)
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
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
				.upLinkRel("up")
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
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2014-01-31 05:00:00", SQL_FORMATTER))
				.enabled(Boolean.FALSE)
				.firstName("First")
				.lastName("last")
				.password("password")
				.role("whatever")
				.username("aUsername")
				.build())
			.amiBillingReady(AmIBillingReadyKind.OPERABLE)
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
						.build()
				)
			))
			.aggregateNodeRefs(new HashSet<>(
				Collections.singletonList(
					AggregateNodeRef.builder()
						.ref("ref")
						.build()
				)
			))
			.build();
	}

	public static void hydrateConnectedUsagePointEntities(UsagePoint up, String num) {
		up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));

		ServiceDeliveryPoint sdp = up.getServiceDeliveryPoint();

		sdp.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, "UUID"+num));

		sdp.setUsagePoints(new HashSet<>(
			Collections.singletonList(up)
		));

		TimeConfiguration tc = up.getTimeConfiguration();

		tc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, tc.getSelfLinkHref()));

		tc.setUsagePoints(new HashSet<>(
			Collections.singletonList(up)
		));

		RetailCustomer rc = up.getRetailCustomer();

		rc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rc.getSelfLinkHref()));

		rc.setUsagePoints(new HashSet<>(
			Collections.singletonList(up)
		));

	}
}
