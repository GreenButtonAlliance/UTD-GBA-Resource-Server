package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.ApplicationInformation;
import org.greenbuttonalliance.gbaresourceserver.usage.model.Authorization;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@XmlRootElement(name = "ApplicationInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInformationDto extends IdentifiedObjectDto implements Serializable {
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
