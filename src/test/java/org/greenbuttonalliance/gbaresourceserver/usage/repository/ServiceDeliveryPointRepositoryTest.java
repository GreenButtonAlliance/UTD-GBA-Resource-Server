package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.common.model.TariffRiderRef;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ServiceDeliveryPoint;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
import java.util.stream.Stream;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceDeliveryPointRepositoryTest {

	private final ServiceDeliveryPointRepository serviceDeliveryPointRepository;

	// for testing findById

	private static final String UUID_PARAMETER = "SDPTest";

	private static final String PRESENT = "SDPTest1";

	private static final String NOT_PRESENT = "foobar";

	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		serviceDeliveryPointRepository.deleteAllInBatch();
		serviceDeliveryPointRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT);
		UUID foundUuid = serviceDeliveryPointRepository.findById(presentUuid).map(ServiceDeliveryPoint::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT);
		Optional<ServiceDeliveryPoint> serviceDeliveryPoint = serviceDeliveryPointRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			serviceDeliveryPoint.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, serviceDeliveryPoint.map(ServiceDeliveryPoint::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = serviceDeliveryPointRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		ServiceDeliveryPoint fullyMappedServiceDeliveryPoint = serviceDeliveryPointRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT)).orElse(null);
		Assumptions.assumeTrue(fullyMappedServiceDeliveryPoint != null);

		Function<ServiceDeliveryPoint, Optional<Set<UsagePoint>>> serviceDeliveryPointToUsagePoints = sdp -> Optional.ofNullable(sdp.getUsagePoints());

		Assertions.assertAll(
			"Entity mapping failures for service delivery point " + fullyMappedServiceDeliveryPoint.getUuid(),
			Stream.of(serviceDeliveryPointToUsagePoints)
				.map(mappingFunc ->
					() -> Assertions.assertTrue(mappingFunc.apply(fullyMappedServiceDeliveryPoint).isPresent()))
		);
	}

	private static List<ServiceDeliveryPoint> buildTestData() {
		List<ServiceDeliveryPoint> serviceDeliveryPoints = Arrays.asList(
			ServiceDeliveryPoint.builder()
				.name("name")
				.tariffProfile("tariffProfile")
				.customerAgreement("customerAgreement")
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						UsagePointRepositoryTest.createUsagePoint()
					)))
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
				.build(),

			ServiceDeliveryPoint.builder()
				.name("name")
				.tariffProfile("tariffProfile")
				.customerAgreement("customerAgreement")
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						UsagePointRepositoryTest.createUsagePoint()
					)))
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
				.build(),

			ServiceDeliveryPoint.builder()
				.name("name")
				.tariffProfile("tariffProfile")
				.customerAgreement("customerAgreement")
				.usagePoints(new HashSet<>(
					Collections.singletonList(
						UsagePointRepositoryTest.createUsagePoint()
					)))
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
				.build()
		);

		// hydrate UUIDs and entity mappings
		AtomicInteger count = new AtomicInteger();
		serviceDeliveryPoints.forEach(sdp -> {

			sdp.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, UUID_PARAMETER+count));

			UsagePoint up = sdp.getUsagePoints().stream().toList().get(0);

			up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));

			up.setServiceDeliveryPoint(sdp);

			count.getAndIncrement();

			UsagePointRepositoryTest.hydrateConnectedUsagePointEntities(up, count.toString());

			UsagePointRepositoryTest.connectUsagePoint(up);
		});
		return serviceDeliveryPoints;
	}
}
