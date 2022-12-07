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
public class PerCent {
	@Column
	private int percent;

	public void setPercent(int percent) {
		if(percent >= 0 && percent <= 100) {
			this.percent = percent;
		}

		else {
			throw new IllegalArgumentException("percent must be between 0 and 100 inclusive.");
		}
	}
}
