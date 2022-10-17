package org.greenbuttonalliance.gbaresourceserver.usage.service;

import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.TimeConfiguration;
import org.greenbuttonalliance.gbaresourceserver.usage.repository.TimeConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeConfigurationService {
	private final TimeConfigurationRepository timeConfigurationRepository;

	public Optional<TimeConfiguration> findByUuid(UUID uuid) {
		return timeConfigurationRepository.findById(uuid);
	}

	public List<TimeConfiguration> findAll() {
		return timeConfigurationRepository.findAll();
	}
}
