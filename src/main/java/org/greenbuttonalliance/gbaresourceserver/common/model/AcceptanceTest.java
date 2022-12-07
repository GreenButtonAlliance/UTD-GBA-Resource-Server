package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptanceTest {

	@Column
	private String type;

	@Column
	private Boolean success;

	@Column
	private Long dateTime; // in seconds
}
