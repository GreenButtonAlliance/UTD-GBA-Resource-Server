/*
 * Copyright (c) 2022 Green Button Alliance, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.DataCustodianApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.GrantType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ResponseType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationStatus;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationType;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ThirdPartyApplicationUse;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.TokenEndpointMethod;
import org.hibernate.annotations.ColumnTransformer;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_information", schema = "usage")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class ApplicationInformation extends IdentifiedObject {

	@Column(name = "authorization_server_authorization_endpoint", nullable = false)
	private String authorizationServerAuthorizationEndpoint;

	@Column(name = "authorization_server_registration_endpoint")
	private String authorizationServerRegistrationEndpoint;

	@Column(name = "authorization_server_token_endpoint", nullable = false)
	private String authorizationServerTokenEndpoint;

	@Column(name = "authorization_server_uri")
	private String authorizationServerUri;

	@Column(name = "client_id", nullable = false)
	private String clientId;

	@Column(name = "client_id_issued_at", nullable = false)
	private Long clientIdIssuedAt; // in epoch-seconds

	@Column(name = "client_name", nullable = false)
	private String clientName;

	@Column(name = "client_secret", nullable = false)
	private String clientSecret;

	@Column(name = "client_secret_expires_at", nullable = false)
	private Long clientSecretExpiresAt; // in epoch-seconds

	@Column(name = "client_uri")
	private String clientUri;

	@ElementCollection
	@CollectionTable(name = "application_information_contact", schema = "usage", joinColumns = {@JoinColumn(name = "application_information_uuid", nullable = false)})
	@Column(name = "contact", nullable = false)
	private Set<String> contacts = new HashSet<>();

	@Column(name = "data_custodian_application_status", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.data_custodian_application_status)", read = "data_custodian_application_status::TEXT")
	private DataCustodianApplicationStatus dataCustodianApplicationStatus;

	@Column(name = "data_custodian_bulk_request_uri", nullable = false)
	private String dataCustodianBulkRequestUri;

	@Column(name = "data_custodian_id", nullable = false)
	private String dataCustodianId;

	@Column(name = "data_custodian_resource_endpoint", nullable = false)
	private String dataCustodianResourceEndpoint;

	@Column(name = "third_party_scope_selection_screen_uri")
	private String thirdPartyScopeSelectionScreenUri;

	@Column(name = "third_party_user_portal_screen_uri")
	private String thirdPartyUserPortalScreenUri;

	@Column(name = "logo_uri")
	private String logoUri;

	@Column(name = "policy_uri")
	private String policyUri;

	@Column(name = "third_party_application_description")
	private String thirdPartyApplicationDescription;

	@Column(name = "third_party_application_status")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.third_party_application_status)", read = "third_party_application_status::TEXT")
	private ThirdPartyApplicationStatus thirdPartyApplicationStatus;

	@Column(name = "third_party_application_type")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.third_party_application_type)", read = "third_party_application_type::TEXT")
	private ThirdPartyApplicationType thirdPartyApplicationType;

	@Column(name = "third_party_application_use")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.third_party_application_use)", read = "third_party_application_use::TEXT")
	private ThirdPartyApplicationUse thirdPartyApplicationUse;

	@Column(name = "third_party_phone")
	private String thirdPartyPhone;

	@Column(name = "third_party_notify_uri", nullable = false)
	private String thirdPartyNotifyUri;

	@ElementCollection
	@CollectionTable(name = "application_information_redirect_uri", schema = "usage", joinColumns = {@JoinColumn(name = "application_information_uuid", nullable = false)})
	@Column(name = "redirect_uri", nullable = false)
	private Set<String> redirectUris = new HashSet<>();

	@Column(name = "tos_uri")
	private String tosUri;

	@Column(name = "software_id", nullable = false)
	private String softwareId;

	@Column(name = "software_version", nullable = false)
	private String softwareVersion;

	@Column(name = "token_endpoint_auth_method", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.token_endpoint_method)", read = "token_endpoint_auth_method::TEXT")
	private TokenEndpointMethod tokenEndpointAuthMethod;

	@Column(name = "response_type")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.response_type)", read = "response_type::TEXT")
	private ResponseType responseType;

	@Column(name = "registration_client_uri", nullable = false)
	private String registrationClientUri;

	@Column(name = "registration_access_token", nullable = false)
	private String registrationAccessToken;

	@Column(name = "data_custodian_scope_selection_screen_uri")
	private String dataCustodianScopeSelectionScreenUri;

	@ElementCollection
	@CollectionTable(name = "application_information_grant_type", schema = "usage", joinColumns = {@JoinColumn(name = "application_information_uuid", nullable = false)})
	@Column(name = "grant_type", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.grant_type)", read = "grant_type::TEXT")
	private Set<GrantType> grantTypes = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "application_information_scope", schema = "usage", joinColumns = {@JoinColumn(name = "application_information_uuid", nullable = false)})
	@Column(name = "scope", nullable = false)
	private Set<String> scopes = new HashSet<>();

	//TODO Add connection to subscription class
}
