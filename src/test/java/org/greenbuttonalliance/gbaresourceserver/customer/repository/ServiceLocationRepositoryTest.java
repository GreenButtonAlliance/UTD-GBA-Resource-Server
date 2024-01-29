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

package org.greenbuttonalliance.gbaresourceserver.customer.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.customer.model.ElectronicAddress;
import org.greenbuttonalliance.gbaresourceserver.customer.model.PositionPoint;
import org.greenbuttonalliance.gbaresourceserver.customer.model.ServiceLocation;
import org.greenbuttonalliance.gbaresourceserver.customer.model.Status;
import org.greenbuttonalliance.gbaresourceserver.customer.model.StreetAddress;
import org.greenbuttonalliance.gbaresourceserver.customer.model.StreetDetail;
import org.greenbuttonalliance.gbaresourceserver.customer.model.TelephoneNumber;
import org.greenbuttonalliance.gbaresourceserver.customer.model.TownDetail;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceLocationRepositoryTest {
	@Autowired
	private ServiceLocationRepository serviceLocationRepository;
	private static final String PRESENT_SELF_LINK =
		"https://data.greenbuttonconnect.org/DataCustodian/DataCustodian/espi/1_1/resource/ApplicationInformation/1";
	private static final String NOT_PRESENT_TITLE = "bar";


	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@BeforeEach
	public void initTestData() {
		serviceLocationRepository.deleteAllInBatch();
		serviceLocationRepository.saveAll(buildTestData());
	}

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = serviceLocationRepository.findById(presentUuid).map(ServiceLocation::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_TITLE);
		Optional<ServiceLocation> serviceLocation = serviceLocationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			serviceLocation.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, serviceLocation.map(ServiceLocation::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = serviceLocationRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {
		ServiceLocation fullyMappedServiceLocation = serviceLocationRepository.findById(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK)).orElse(null);
		Assumptions.assumeTrue(fullyMappedServiceLocation != null);
		//first level

		Function<ServiceLocation,Optional<StreetAddress>> serviceLocationToMainAddress=sl->Optional.ofNullable(sl.getMainAddress());
		Function<ServiceLocation,Optional<StreetAddress>> serviceLocationToSecondaryAddress= sl->Optional.ofNullable(sl.getSecondaryAddress());
		Function<ServiceLocation,Optional<TelephoneNumber>> serviceLocationToTelephoneNumber1 = sl-> Optional.ofNullable(sl.getPhone1());
		Function<ServiceLocation,Optional<TelephoneNumber>> serviceLocationToTelephoneNumber2 = sl-> Optional.ofNullable(sl.getPhone2());
		Function<ServiceLocation,Optional<ElectronicAddress>> serviceLocationToElectronicAddress=sl->Optional.ofNullable(sl.getElectronicAddress());

		//level 2
		//from ServiceLocation.Mainaddress and ServiceLocation.SecondaryAddress
		Function<ServiceLocation,Optional<StreetDetail>> serviceLocationToMainStreetAddressStreetDetail =serviceLocationToMainAddress.andThen(opt->opt.map(
			StreetAddress::getStreetDetail
		));
		Function<ServiceLocation,Optional<StreetDetail>> serviceLocationToSecondaryStreetAddressStreetDetail= serviceLocationToSecondaryAddress.andThen(opt->opt.map(
			StreetAddress::getStreetDetail
		));
		Function<ServiceLocation,Optional<TownDetail>> serviceLocationToMainStreetAddressTownDetail=serviceLocationToMainAddress.andThen(opt->opt.map(
			StreetAddress::getTownDetail
		));
		Function<ServiceLocation,Optional<TownDetail>> serviceLocationToSecondaryStreetAddressTownDetail=serviceLocationToSecondaryAddress.andThen(opt->opt.map(
			StreetAddress::getTownDetail
		));
		Function<ServiceLocation,Optional<Status>> serviceLocationToMainStreetAddressStatus=serviceLocationToMainAddress.andThen(opt->opt.map(
			StreetAddress::getStatus
		));
		Function<ServiceLocation,Optional<Status>> serviceLocationToSecondaryStreetAddressStatus=serviceLocationToSecondaryAddress.andThen(opt->opt.map(
			StreetAddress::getStatus
		));

		Assertions.assertAll(
			"Entity mapping failures for customer account " + fullyMappedServiceLocation.getUuid(),
			Stream.of(serviceLocationToMainAddress,
				serviceLocationToSecondaryAddress,
				serviceLocationToTelephoneNumber1,
				serviceLocationToTelephoneNumber2,
				serviceLocationToElectronicAddress,
				serviceLocationToMainStreetAddressStreetDetail,
				serviceLocationToSecondaryStreetAddressStreetDetail,
				serviceLocationToMainStreetAddressTownDetail,
				serviceLocationToSecondaryStreetAddressTownDetail,
				serviceLocationToMainStreetAddressStatus,
				serviceLocationToSecondaryStreetAddressStatus
			)
				.map(mappingFunc->()->Assertions.assertTrue(mappingFunc.apply(fullyMappedServiceLocation).isPresent()))
		);
	}
	private static List<ServiceLocation> buildTestData() {
		//TODO: Add EndDevice to test ServiceLocation to EndDevice mapping
		//TODO: Add Meter to test ServiceLocation to Meter mapping
		//TODO: Add TimeConfiguration to test ServiceLocation to TimeConfiguration mapping
		//TODO: Add CustomerAgreement to test ServiceLocation to CustomerAgreement mapping
		List<ServiceLocation> serviceLocations = Arrays.asList(
			ServiceLocation.builder()
				.description("test")
				.selfLinkHref(PRESENT_SELF_LINK)
				.upLinkHref("https://data.greenbuttonconnect.org/DataCustodian/DataCustodian/espi/1_1/resource/" +
					"ApplicationInformation")
				.type("Lorem")
				.mainAddress(
					StreetAddress.builder()
						.streetDetail(
							StreetDetail.builder()
								.number("ipsum ")
								.name("dolor  ")
								.suffix("sit ")
								.prefix("amet")
								.type("consectetur ")
								.code("adipiscing  ")
								.buildingName("elit")
								.suiteNumber("sed ")
								.addressGeneral("do ")
								.addressGeneral2("eiusmod ")
								.addressGeneral3("tempor ")
								.withinTownLimits(true)
								.build()
						)
						.townDetail(
							TownDetail.builder()
								.code("incididunt ")
								.section("ut ")
								.name("labore ")
								.county("et ")
								.stateOrProvince("dolore ")
								.country("magna ")
								.build()
						)
						.status(
							Status.builder()
								.value("Eu ")
								.dateTime(400000L)
								.remark("consequat ")
								.reason("ac ")
								.build()
						)
						.postalCode("felis ")
						.poBox("donec")
						.build()

				)
				.secondaryAddress(
					StreetAddress.builder()

						.streetDetail(
							StreetDetail.builder()
								.number("Aliquet ")
								.name("risus")
								.suffix("feugiat ")
								.prefix("in")
								.type("ante ")
								.code("metus ")
								.buildingName("dictum")
								.suiteNumber("at")
								.addressGeneral("tempor")
								.addressGeneral2("commodo")
								.addressGeneral3("Elit")
								.withinTownLimits(true)
								.build()
						)
						.townDetail(
							TownDetail.builder()
								.code("ut")
								.section("aliquam")
								.name("purus")
								.county("memoria")
								.stateOrProvince("Venenatis ")
								.country("nascetur ")
								.build()
						)
						.status(
							Status.builder()
								.value("nascetur")
								.dateTime(800L)
								.remark("irum ")
								.reason("rhoncus")
								.build()
						)
						.postalCode("felis ")
						.poBox("rex")
						.build()
				)
				.phone1(
					TelephoneNumber.builder()
						.countryCode("Ultricies")
						.areaCode("quis ")
						.cityCode("hendrerit")
						.localNumber("dolor")
						.ext("magna")
						.dialOut("bagel")
						.internationalPrefix("eget")
						.ituPhone("est")
						.build()
				)
				.phone2(
					TelephoneNumber.builder()
						.countryCode("lorem")
						.areaCode("Erat")
						.cityCode("pellentesque")
						.localNumber("adipiscing ")
						.ext("commodo ")
						.dialOut("elit ")
						.internationalPrefix("imperdiet ")
						.ituPhone("dui ")
						.build()
				)
				.electronicAddress(
					ElectronicAddress.builder()
						.lan("Nulla ")
						.mac("facilisi ")
						.email1("nullam ")
						.email2("vehicula")
						.web("ipsum ")
						.radio("arcu ")
						.userId("cursus ")
						.build()
				)
				.geoInfoReference("vitae")
				.direction("down")
				.status("Adipiscing ")
				.positionPoint(
					PositionPoint.builder()
						.xPosition("2")
						.yPosition("-2")
						.zPosition("NaN")
						.build()
				)
				.accessMethod("Foo")
				.siteAccessProblem("bar")
				.needsInspection(false)
				.usagePoints(new HashSet<>(Arrays.asList(
					"up4",
					"up5",
					"up6"
					))

				)
				.outageBlock("foobar")
				.build(),
			ServiceLocation.builder()
				.description("test")
				.selfLinkHref("https://data.greenbuttonconnect.org/DataCustodian/DataCustodian/espi/1_1/resource/" +
					"ApplicationInformation/2")
				.upLinkHref("https://data.greenbuttonconnect.org/DataCustodian/DataCustodian/espi/1_2/resource/" +
					"ApplicationInformation")
				.type("Lorem")
				.mainAddress(
					StreetAddress.builder()
						.streetDetail(
							StreetDetail.builder()
								.number("ipsum ")
								.name("dolor  ")
								.suffix("sit ")
								.prefix("amet")
								.type("consectetur ")
								.code("adidad")
								.buildingName("elit")
								.suiteNumber("sed ")
								.addressGeneral("do ")
								.addressGeneral2("eiusmod ")
								.addressGeneral3("tempor ")
								.withinTownLimits(true)
								.build()
						)
						.townDetail(
							TownDetail.builder()
								.code("incididunt ")
								.section("ut ")
								.name("labore ")
								.county("et ")
								.stateOrProvince("dolore ")
								.country("magna ")
								.build()
						)
						.status(
							Status.builder()
								.value("Eu ")
								.dateTime(400000L)
								.remark("consequat ")
								.reason("ac ")
								.build()
						)
						.postalCode("felis ")
						.poBox("donec")
						.build()

				)
				.secondaryAddress(
					StreetAddress.builder()
						.streetDetail(
							StreetDetail.builder()
								.number("Aliquet ")
								.name("fallus")
								.suffix("feugiat ")
								.prefix("in")
								.type("ante ")
								.code("metus ")
								.buildingName("dictum")
								.suiteNumber("at")
								.addressGeneral("tempor")
								.addressGeneral2("commodo")
								.addressGeneral3("Elit")
								.withinTownLimits(true)
								.build()
						)
						.townDetail(
							TownDetail.builder()
								.code("ut")
								.section("aliquam")
								.name("purus")
								.county("memoria")
								.stateOrProvince("Venenatis ")
								.country("ex")
								.build()
						)
						.status(
							Status.builder()
								.value("nascetur")
								.dateTime(456789L)
								.remark("irum ")
								.reason("rhoncus")
								.build()
						)
						.postalCode("felis ")
						.poBox("rex")
						.build()
				)
				.phone1(
					TelephoneNumber.builder()
						.countryCode("Ultricies")
						.areaCode("quis ")
						.cityCode("helperit")
						.localNumber("dolor")
						.ext("magna")
						.dialOut("bagel")
						.internationalPrefix("eget")
						.ituPhone("est")
						.build()
				)
				.phone2(
					TelephoneNumber.builder()
						.countryCode("lorem")
						.areaCode("Erat")
						.cityCode("pellentesque")
						.localNumber("adipiscing ")
						.ext("commodo ")
						.dialOut("elit ")
						.internationalPrefix("imperdiet ")
						.ituPhone("zwei")
						.build()
				)
				.electronicAddress(
					ElectronicAddress.builder()
						.lan("Nutella")
						.mac("facilisi ")
						.email1("nullam")
						.email2("vehicula")
						.web("ipsum")
						.radio("arcu")
						.userId("cursus ")
						.build()
				)
				.geoInfoReference("vitae")
				.direction("down")
				.status("Adipiscing ")
				.positionPoint(
					PositionPoint.builder()
						.xPosition("pi")
						.yPosition("e")
						.zPosition("i")
						.build()
				)
				.accessMethod("Foo")
				.siteAccessProblem("bar")
				.needsInspection(false)
				.usagePoints(new HashSet<>(Arrays.asList(
						"up1",
						"up2",
						"up3"
					))

				)
				.outageBlock("foobar")
				.build()
		);
		serviceLocations.forEach(ai -> {
			ai.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ai.getSelfLinkHref()));
			}

		);

		return serviceLocations;
	}
}
