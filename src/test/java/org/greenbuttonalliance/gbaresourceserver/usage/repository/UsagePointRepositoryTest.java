package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.common.model.AggregateNodeRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.PnodeRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.TariffRiderRef;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ServiceDeliveryPoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsageSummary;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AmIBillingReadyKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.PhaseCodeKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.UsagePointConnectedKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

	private static List<UsagePoint> buildTestData() {
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
			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));
			ServiceDeliveryPoint sdp = up.getServiceDeliveryPoint();
			count.getAndIncrement();
			sdp.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, "UUID"+count));
			//TODO: have sdp add up as well
		});

		return usagePoints;
	}
}
