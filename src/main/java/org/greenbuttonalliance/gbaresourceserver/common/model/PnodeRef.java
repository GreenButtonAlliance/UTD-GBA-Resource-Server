package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.ApnodeType;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "pnode_ref", schema = "usage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PnodeRef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "apnode_type")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.apnode_type)", read = "apnode_type::TEXT")
	private ApnodeType apnodeType;

	@Column
	private String ref;

	@Column(name = "start_effective_date")
	private Long startEffectiveDate; //in epoch-seconds

	@Column(name = "end_effective_date")
	private Long endEffectiveDate; //in epoch-seconds
}
