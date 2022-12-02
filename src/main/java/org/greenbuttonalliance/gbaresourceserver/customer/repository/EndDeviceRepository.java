package org.greenbuttonalliance.gbaresourceserver.customer.repository;

import org.greenbuttonalliance.gbaresourceserver.customer.model.EndDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EndDeviceRepository extends JpaRepository<EndDevice, UUID> {
}
