/*
 * Copyright (c) 2022-2023 Green Button Alliance, Inc.
 *
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

package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@XmlRootElement(name = "ApplicationInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInformationDto extends IdentifiedObjectDto implements Serializable {
	//TODO add XML Annotations to all data elements
	private String dataCustodianId;
	private DataCustodianApplicationStatus dataCustodianApplicationStatus;
	private String thirdPartyApplicationDescription;
	private ThirdPartyApplicationStatus thirdPartyApplicationStatus;
	private ThirdPartyApplicationType thirdPartyApplicationType;
	private ThirdPartyApplicationUse thirdPartyApplicationUse;
	// does anyURI on the standard mean string?
	private String thirdPartyPhone;
	private String authorizationServerUri;
	private String thirdPartyNotifyUri;
	private String authorizationServerAuthorizationEndpoint;
	private String authorizationServerRegistrationEndpoint;
	private String authorizationServerTokenEndpoint;
	private String dataCustodianBulkRequestURI;
	private String dataCustodianResourceEndpoint;
	private String thirdPartyScopeSelectionURI;
	private String thirdPartyUserPortalScreenURI;
	private String client_secret;
	private String logo_uri;
	private String client_name;
	private String client_uri;
	private String redirect_uri;
	private String client_id;
	private String tos_uri;
	private String policy_uri;
	private String software_id;

	public static ApplicationInformationDto fromApplicationInformation(ApplicationInformation applicationInformation) {
		return Optional.ofNullable(applicationInformation)
			.map(ai -> new IdentifiedObjectDtoSubclassFactory<>(ApplicationInformationDto::new).create(ai)
				.setDataCustodianId(ai.getDataCustodianId())
				.setDataCustodianApplicationStatus(ai.getDataCustodianApplicationStatus())
				.setThirdPartyApplicationDescription(ai.getThirdPartyApplicationDescription())
				.setThirdPartyApplicationStatus(ai.getThirdPartyApplicationStatus())
				.setThirdPartyApplicationType(ai.getThirdPartyApplicationType())
				.setThirdPartyApplicationUse(ai.getThirdPartyApplicationUse())
				.setThirdPartyPhone(ai.getThirdPartyPhone())
				.setAuthorizationServerUri(ai.getAuthorizationServerUri())
				.setThirdPartyNotifyUri(ai.getThirdPartyNotifyUri())
				.setAuthorizationServerAuthorizationEndpoint(ai.getAuthorizationServerAuthorizationEndpoint())
				.setAuthorizationServerRegistrationEndpoint(ai.getAuthorizationServerRegistrationEndpoint())
				.setAuthorizationServerTokenEndpoint(ai.getAuthorizationServerTokenEndpoint())
				.setDataCustodianBulkRequestURI(ai.getDataCustodianBulkRequestURI())
				.setDataCustodianResourceEndpoint(ai.getDataCustodianResourceEndpoint())
				.setThirdPartyScopeSelectionURI(ai.getThirdPartyScopeSelectionURI())
				.setThirdPartyUserPortalScreenURI(ai.getThirdPartyUserPortalScreenURI())
				.setClient_secret(ai.getClient_secret())
				.setLogo_uri(ai.getLogo_uri())
				.setClient_name(ai.getClient_name())
				.setClient_uri(ai.getClient_uri())
				.setRedirect_uri(ai.getRedirect_uri())
				.setClient_id(ai.getClient_id())
				.setTos_uri(ai.getTos_uri())
				.setPolicy_uri(ai.getPolicy_uri())
				.setSoftware_id(ai.getSoftware_id())
				)
			.orElse(null);
	}
}
