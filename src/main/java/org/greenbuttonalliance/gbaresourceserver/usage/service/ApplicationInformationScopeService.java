/*
 *
 *  * Copyright (c) 2022 Green Button Alliance, Inc.
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package org.greenbuttonalliance.gbaresourceserver.usage.service;

import lombok.RequiredArgsConstructor;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformationGrantTypes;

import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformationScope;
import org.greenbuttonalliance.gbaresourceserver.usage.repository.ApplicationInformationScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationInformationScopeService {
	private final ApplicationInformationScopeRepository applicationInformationScopeRepository;

	public Optional<ApplicationInformationScope> findByUuid(UUID uuid)
	{
		return applicationInformationScopeRepository.findById(uuid);
	}
	public List<ApplicationInformationScope> findAll()
	{
		return applicationInformationScopeRepository.findAll();
	}
}
