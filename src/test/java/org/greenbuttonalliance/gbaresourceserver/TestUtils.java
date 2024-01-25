/*
 * Copyright (c) 2024 Green Button Alliance, Inc.
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

package org.greenbuttonalliance.gbaresourceserver;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.common.model.SummaryMeasurement;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.Currency;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.EnrollmentStatus;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitMultiplierKind;
import org.greenbuttonalliance.gbaresourceserver.common.model.enums.UnitSymbolKind;
import org.greenbuttonalliance.gbaresourceserver.usage.model.*;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Donald F. Coffin, Green Button Alliance, Inc.
 */
public class TestUtils {
	private static final String upLinkHref = "https://data.greenbuttonconnect.org/" +
											 "DataCustodian/espi/1_1/resource/UsagePoint";
	private static final String PRESENT_SELF_LINK = "https://data.greenbuttonconnect.org/" +
													"DataCustodian/espi/1_1/resource" +
													"/UsagePoint/123456";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static UsagePoint createUsagePoint() {

		byte[] deadbeefs = BigInteger.valueOf(Long.parseLong("DEADBEEF", 16)).toByteArray();

		return UsagePoint.builder()
			.description("description")
			.published(LocalDateTime.parse("2022-03-01 05:00:00", SQL_FORMATTER))
			.selfLinkHref(PRESENT_SELF_LINK)
			.upLinkHref(upLinkHref)
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
				.description("description")
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
									.id(200000L)
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
						.id(4L)
						.apnodeType(ApnodeType.AG)
						.ref("Pnode_4")
						.build()
				)
			))
			.aggregateNodeRefs(new HashSet<>(
				Collections.singletonList(
					AggregateNodeRef.builder()
						.anodeType(AnodeType.AGR)
						.ref("Anode_4")
						.build()
				)
			))
			.build();
	}

	public static void hydrateConnectedUsagePointEntities(UsagePoint up, String num) {
		up.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, up.getSelfLinkHref()));

		ServiceDeliveryPoint sdp = up.getServiceDeliveryPoint();

		if (sdp.getUuid() == null) {
			sdp.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, "SDPTest" + num));
		}

		TimeConfiguration tc = up.getTimeConfiguration();

		tc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, tc.getSelfLinkHref()));

		RetailCustomer rc = up.getRetailCustomer();

		rc.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rc.getSelfLinkHref()));

		Optional.ofNullable(up.getMeterReadings()).ifPresent(mrs -> {
			mrs.forEach(mr -> {
				mr.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, mr.getSelfLinkHref()));


				Optional.ofNullable(mr.getIntervalBlocks()).ifPresent(ibs ->
					ibs.forEach(ib -> {
							ib.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ib.getSelfLinkHref()));
							ib.setMeterReading(mr);
						}
					));

				Optional.ofNullable(mr.getReadingType()).ifPresent(rt -> {
					rt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rt.getSelfLinkHref()));
					rt.setMeterReading(mr);
				});
			});
		});

		Optional.ofNullable(up.getElectricPowerQualitySummaries()).ifPresent(epqss -> {
			epqss.forEach(epqs -> {
				epqs.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, epqs.getSelfLinkHref()));
			});
		});

		AtomicInteger count = new AtomicInteger();
		Optional.ofNullable(up.getUsageSummaries()).ifPresent(uss -> {
			uss.forEach(us -> {
				us.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, us.getSelfLinkHref()));


				Optional.ofNullable(us.getLineItems()).ifPresent(lis ->
					lis.forEach(li ->
						li.setUsageSummary(us)));
			});
		});
	}

	public static void connectUsagePoint(UsagePoint up) {
		ServiceDeliveryPoint sdp = up.getServiceDeliveryPoint();
		sdp.setUsagePoints(new HashSet<>(
			Collections.singletonList(up)
		));

		TimeConfiguration tc = up.getTimeConfiguration();
		tc.setUsagePoints(new HashSet<>(
			Collections.singletonList(up)
		));

		RetailCustomer rc = up.getRetailCustomer();
		rc.setUsagePoints(new HashSet<>(
			Collections.singletonList(up)
		));

		Optional.ofNullable(up.getMeterReadings()).ifPresent(mrs -> {
			mrs.forEach(mr -> {
				mr.setUsagePoint(up);

				Optional.ofNullable(mr.getIntervalBlocks()).ifPresent(ibs ->
					ibs.forEach(ib -> {
							ib.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ib.getSelfLinkHref()));
							ib.setMeterReading(mr);
						}
					));

				Optional.ofNullable(mr.getReadingType()).ifPresent(rt -> {
					rt.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, rt.getSelfLinkHref()));
					rt.setMeterReading(mr);
				});
			});
		});

		up.getElectricPowerQualitySummaries().forEach(epqs -> {
			epqs.setUsagePoint(up);
		});

		Optional.ofNullable(up.getUsageSummaries()).ifPresent(uss -> {
			uss.forEach(us -> {
				AtomicInteger count = new AtomicInteger();
				us.setUsagePoint(up);

				Optional.ofNullable(us.getLineItems()).ifPresent(lis ->
					lis.forEach(li -> {
						li.setUsageSummary(us);
					}));
			});
		});
	}
}
