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

package org.greenbuttonalliance.gbaresourceserver.testutils;

import com.github.f4b6a3.uuid.UuidCreator;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;

/**
 * @author Donald F. Coffin, Green Button Alliance, Inc.
 */
public class RetailCustomerCreator {
	public static RetailCustomer create(String selfLinkHref, String upLinkHref, String firstName, String lastName,
										String username) {
		return RetailCustomer.builder()
			.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, selfLinkHref))
			.description("Retail Customer Record")
			.selfLinkHref(selfLinkHref)
			.upLinkHref(upLinkHref)
			.enabled(true)
			.firstName(firstName)
			.lastName(lastName)
			.password("koala")
			.role("ROLE_USER")
			.username(username)
			.build();
	}
}
