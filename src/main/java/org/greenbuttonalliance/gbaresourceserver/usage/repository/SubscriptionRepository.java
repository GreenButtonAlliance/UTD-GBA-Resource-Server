package org.greenbuttonalliance.gbaresourceserver.usage.repository;

import org.greenbuttonalliance.gbaresourceserver.usage.model.ElectricPowerQualitySummary;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
