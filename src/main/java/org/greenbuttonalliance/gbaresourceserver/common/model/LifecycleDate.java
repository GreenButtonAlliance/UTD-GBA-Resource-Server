package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LifecycleDate {

	@Column(name = "manufactured_date")
	private Long manufacturedDate; // in seconds

	@Column(name = "purchase_date")
	private Long purchaseDate; // in seconds

	@Column(name = "received_date")
	private Long receivedDate; // in seconds

	@Column(name = "installation_date")
	private Long installationDate; // in seconds

	@Column(name = "removal_date")
	private Long removalDate; // in seconds

	@Column(name = "retired_date")
	private Long retiredDate; // in seconds
}
