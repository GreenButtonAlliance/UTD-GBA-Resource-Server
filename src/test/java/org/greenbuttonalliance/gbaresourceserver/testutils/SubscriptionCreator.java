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
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.RetailCustomer;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Subscription;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.greenbuttonalliance.gbaresourceserver.usage.model.UsagePoint;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Donald F. Coffin, Green Button Alliance, Inc.
 */
public class SubscriptionCreator {
	public static Set<Subscription> createSubscriptions(int count, Integer UUIDIndex, String description,
														LocalDateTime published, String selfLinkHref,
														String upLinkHref, LocalDateTime updated,
														String hashedId, ApplicationInformation applicationInformation,
														Authorization authorization, RetailCustomer retailCustomer,
														UsagePoint usagePoint, TimeConfiguration timeConfiguration) {
		Set<Subscription> subscriptions = new HashSet<>();
		for (int i = 0; i < count; i++) {
			if (!(UUIDIndex == null)) {
				selfLinkHref = selfLinkHref + (UUIDIndex + count);
			}
			Subscription subscription = Subscription.builder()
				.uuid(UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, selfLinkHref))
				.description(description)
				.published(published)
				.selfLinkHref(selfLinkHref)
				.upLinkHref(upLinkHref)
				.updated(updated)
				.hashedId(hashedId)
				.applicationInformation(applicationInformation)
				.authorization(authorization)
				.retailCustomer(retailCustomer)
				.usagePoint(usagePoint)
				.timeConfiguration(timeConfiguration)
				.build();
			subscriptions.add(subscription);
		}
		return subscriptions;
	}
}
