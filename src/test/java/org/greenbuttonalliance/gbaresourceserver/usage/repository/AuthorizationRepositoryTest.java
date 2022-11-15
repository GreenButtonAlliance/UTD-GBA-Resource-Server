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

package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.OAuthError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationRepositoryTest {
	private final AuthorizationRepository authorizationRepository;

	private static final String PRESENT_SELF_LINK = "https://{domain}/DataCustodian/espi/1_1/resource/Authorization/1";
	private static final String NOT_PRESENT_SELF_LINK = "foobar";
	private static final String DUMMY_STRING = "test1";
	private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void initTestData() {
		authorizationRepository.deleteAllInBatch();
		authorizationRepository.saveAll(buildTestData());
	}

	@Test
	public void findByPresentId_returnsMatching() {
		UUID presentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, PRESENT_SELF_LINK);
		UUID foundUuid = authorizationRepository.findById(presentUuid).map(Authorization::getUuid).orElse(null);

		Assertions.assertEquals(
			presentUuid,
			foundUuid,
			() -> String.format("findById with %s returns entity with ID %s", presentUuid, foundUuid)
		);
	}

	@Test
	public void findByNotPresentId_returnsEmpty() {
		UUID notPresentUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, NOT_PRESENT_SELF_LINK);
		Optional<Authorization> authorization = authorizationRepository.findById(notPresentUuid);

		Assertions.assertTrue(
			authorization.isEmpty(),
			() -> String.format("findById with %s returns entity with ID %s", notPresentUuid, authorization.map(Authorization::getUuid).orElse(null))
		);
	}

	@Test
	public void findAll_returnsAll() {
		int findByAllSize = authorizationRepository.findAll().size();
		int testDataSize = buildTestData().size();

		Assertions.assertEquals(
			findByAllSize,
			testDataSize,
			() -> String.format("findByAll size of %s does not match test data size of %s", findByAllSize, testDataSize)
		);
	}

	@Test
	public void entityMappings_areNotNull() {

	}

	private static List<Authorization> buildTestData() {
		List<Authorization> authorizations = Arrays.asList(
			Authorization.builder()
				.uuid(UUID.randomUUID())
				.description("Green Button Alliance Data Custodian Authorization")
				.published(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.selfLinkHref(PRESENT_SELF_LINK)
				.selfLinkRel("self")
				.upLinkHref("https://{domain}/DataCustodian/espi/1_1/resource/ApplicationInformation")
				.upLinkRel("up")
				.updated(LocalDateTime.parse("2014-01-02 05:00:00", SQL_FORMATTER))
				.accessToken(DUMMY_STRING)
				.authorizationUri(null)
				.apDuration(BigInteger.valueOf(21))
				.apStart(BigInteger.valueOf(654))
				.code(DUMMY_STRING)
				.error(OAuthError.INVALID_CLIENT)
				.errorDescription(DUMMY_STRING)
				.errorUri(DUMMY_STRING)
				.expiresIn(BigInteger.valueOf(32164))
				.grantType(1)
				.ppDuration(BigInteger.valueOf(23123))
				.ppStart(BigInteger.valueOf(3241654))
				.refreshToken("23123124646")
				.resourceUri(DUMMY_STRING)
				.responseType(24)
				.scope(DUMMY_STRING)
				.state(DUMMY_STRING)
				.thirdParty(DUMMY_STRING)
				.tokenType(54)
				.applicationInformationId(UUID.randomUUID())
				.retailCustomerId(UUID.randomUUID())
				.subscriptionId(UUID.randomUUID())
				.build()
		);

		// hydrate UUIDs and entity mappings
		authorizations.forEach(ai -> {
			ai.setUuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, ai.getSelfLinkHref()));

			// TODO hydrate UsagePoint reference once entity is available
		});
		return authorizations;
	}
}
