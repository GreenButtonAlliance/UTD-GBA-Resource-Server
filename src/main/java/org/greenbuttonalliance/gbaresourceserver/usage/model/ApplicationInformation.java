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

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.*;
import org.hibernate.annotations.ColumnTransformer;

import java.util.Set;


@Entity
@Table(name = "application_information", schema = "usage")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class ApplicationInformation extends IdentifiedObject{
	@Column
	private String kind;
	// i can find no information about the kind column in this table in usage.xsd
	@Column(name = "authorization_server_authorization_endpoint")
	private String authorizationServerAuthorizationEndpoint;
	@Column(name = "authorization_server_registration_endpoint")
	private String authorizationServerRegistrationEndpoint;
	@Column(name = "authorization_server_token_endpoint")
	private String authorizationServerTokenEndpoint;
	@Column(name = "authorization_server_uri")
	private String authorizationServerURI;
	@Column(name="client_id")
	private String clientId;
	//Blue diamond means not null, so client id can't be null
	@Column(name="client_id_issued_at")
	private Long clientIdIssuedAt;
	@Column(name="client_name")
	private String clientName;
	@Column(name="client_secret")
	private String clientSecret;
	@Column(name="client_secret_expires_at")
	private Long clientSecretExpiresAt;
	@Column(name="client_uri")
	private String clientURI;
	@Column
	private Byte[] contacts;
	@Column(name = "data_custodian_application_status")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.data_custodian_application_status)", read = "data_custodian_application_status::TEXT")
	private DataCustodianApplicationStatus dataCustodianApplicationStatus;
	@Column(name="data_custodian_bulk_request_uri")
	private String dataCustodianBulkRequestURI;
	@Column(name="data_custodian_default_batch_resource")
	private String dataCustodianDefaultBatchResource;
	@Column(name="data_custodian_default_subscription_resource")
	private String dataCustodianDefaultSubscriptionResource;
	@Column(name="data_custodian_id")
	private String dataCustodianId;
	@Column(name="data_custodian_resource_endpoint")
	private String dataCustodianResourceEndpoint;
	@Column(name="data_custodian_third_party_selection_screen_uri")
	private String dataCustodianThirdPartySelectionScreenURI;
	@Column(name="logo_uri")
	private String logoURI;
	@Column(name="policy_uri")
	private String policyURI;
	@Column(name="third_party_application_description")
	private String thirdPartyApplicationDescription;
	@Column(name="third_party_application_status")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.third_party_application_status)", read = "third_party_application_status::TEXT")
	private ThirdPartyApplicationStatus thirdPartyApplicationStatus;
	@Column(name="third_party_application_type")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.third_party_application_type)", read = "third_party_application_type::TEXT")
	private ThirdPartyApplicationType thirdPartyApplicationType;
	@Column(name="third_party_application_use")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.third_party_application_use)", read = "third_party_application_use::TEXT")
	private ThirdPartyApplicationUse thirdPartyApplicationUse;
	@Column(name="third_party_phone")
	private String thirdPartyPhone;
	@Column(name="third_party_notify_uri")
	private String thirdPartyNotifyUri;
	@Column(name="third_party_scope_selection_screen_uri")
	private String thirdPartyScopeSelectionScreenURI;
	@Column(name="third_party_user_portal_screen")
	private String thirdPartyUserPortalScreen;
	@Column(name="redirect_uri")
	private String redirectURI;
	@Column(name="tos_uri")
	private String tosURI;
	@Column(name="software_id")
	private String softwareID;
	@Column(name="software_version")
	private String softwareVersion;
	@Column (name="token_endpoint_auth_method")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.token_endpoint_method)", read = "token_endpoint_method::TEXT")
	private TokenEndPointMethod tokenEndpointAuthMethod;
	@Column (name="response_types")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.response_types)", read = "response_types::TEXT")
	private ResponseTypes responseTypes;
	@Column(name="registration_client_uri")
	private String registrationClientURI;
	@Column(name="registration_access_token")
	private String registrationAccessToken;

	@OneToMany(mappedBy = "applicationInformation",cascade = CascadeType.ALL)
	Set<ApplicationInformationGrantTypes> grantTypes;

	@OneToMany(mappedBy = "applicableScope",cascade = CascadeType.ALL)
	Set<ApplicationInformationScope> applicationScope;
}
