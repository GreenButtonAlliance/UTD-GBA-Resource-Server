package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeConfigurationTest {
	private final TimeConfigurationRepository timeConfigurationRepository;
	private static final String SELF_LINK= "https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/183";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@BeforeEach
	public void initTestData() {
		timeConfigurationRepository.deleteAllInBatch();
		timeConfigurationRepository.saveAll(buildTestData());
	}


	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, SELF_LINK);
		UUID foundUuid = timeConfigurationRepository.findById(presentUuid).map(TimeConfiguration::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<TimeConfiguration> timeConfiguration = timeConfigurationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			timeConfiguration.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, timeConfiguration.map(TimeConfiguration::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = timeConfigurationRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}



	public static List<TimeConfiguration> buildTestData() {
		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();
		List<TimeConfiguration> timeConfigurations =  Arrays.asList(TimeConfiguration.builder()
			.published(LocalDateTime.parse("2011-07-03 12:09:38", SQL_FORMATTER))
			.selfLinkHref(SELF_LINK)
			.selfLinkRel("self")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
			.upLinkRel("up")
			.updated(LocalDateTime.parse("2015-06-22 12:11:04", SQL_FORMATTER))
			.dstEndRule(deadbeefs)
			.dstOffset(100L)
			.dstStartRule(deadbeefs)
			.tzOffset(10L).build(),
		TimeConfiguration.builder()
			.published(LocalDateTime.parse("2014-11-18 12:20:45", SQL_FORMATTER))
			.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/184")
			.selfLinkRel("self")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
			.upLinkRel("up")
			.updated(LocalDateTime.parse("2015-10-15 12:21:30", SQL_FORMATTER))
			.dstEndRule(deadbeefs)
			.dstOffset(200L)
			.dstStartRule(deadbeefs)
			.tzOffset(20L).build(),
		TimeConfiguration.builder()
			.published(LocalDateTime.parse("2017-10-15 12:23:04", SQL_FORMATTER))
			.selfLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration/185")
			.selfLinkRel("self")
			.upLinkHref("https://{domain}/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/TimeConfiguration")
			.upLinkRel("up")
			.updated(LocalDateTime.parse("2017-10-15 12:23:17", SQL_FORMATTER))
			.dstEndRule(deadbeefs)
			.dstOffset(300L)
			.dstStartRule(deadbeefs)
			.tzOffset(30L).build()
		);
		timeConfigurations.forEach(tc->{
			tc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, tc.getSelfLinkHref()));
		});
		return timeConfigurations;
	}
}

