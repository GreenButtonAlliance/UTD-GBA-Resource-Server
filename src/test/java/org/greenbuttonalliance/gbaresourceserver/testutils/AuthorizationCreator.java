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
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Subscription;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AuthorizationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenType;

/**
 * @author Donald F. Coffin, Green Button Alliance, Inc.
 */
public class AuthorizationCreator {
	public static Authorization create(String description, String selfLinkHref, String upLinkHref,
									   DateTimeInterval authorizedPeriod, AuthorizationStatus status, long expiresAt,
									   String scope, TokenType tokenType, String resourceUri, String authorizationUri,
									   String customerResourceUri, ApplicationInformation applicationInformation,
									   RetailCustomer retailCustomer, Subscription subscription) {
		return Authorization.builder()
			.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, selfLinkHref))
			.description(description)
			.selfLinkHref(selfLinkHref)
			.upLinkHref(upLinkHref)
			.authorizedPeriod(authorizedPeriod)
			.status(status)
			.expiresAt(expiresAt)
			.scope(scope)
			.tokenType(tokenType)
			.resourceUri(resourceUri)
			.authorizationUri(authorizationUri)
			.customerResourceUri(customerResourceUri)
			.applicationInformation(applicationInformation)
			.retailCustomer(retailCustomer)
			.subscription(subscription)
			.build();
	}
}
