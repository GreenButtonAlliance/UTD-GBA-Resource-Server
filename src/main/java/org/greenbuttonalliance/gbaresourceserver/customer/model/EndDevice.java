package org.greenbuttonalliance.gbaresourceserver.customer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "end_device", schema = "customer")
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class EndDevice extends AssetContainer{

	@Column(name = "is_virtual")
	private Boolean isVirtual;

	@Column(name = "is_pan")
	private Boolean isPan;

	@Column(name = "install_code")
	private String installCode;

	@Column(name = "amr_system")
	private String amrSystem;
}
