package org.greenbuttonalliance.gbaresourceserver.usage.repository;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeConfigurationRepository extends JpaRepository<TimeConfiguration, UUID> {
}
