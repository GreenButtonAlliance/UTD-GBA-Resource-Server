package org.greenbuttonalliance.gbaresourceserver.common.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenbuttonalliance.gbaresourceserver.usage.model.enums.AnodeType;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "aggregate_node_ref", schema = "usage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregateNodeRef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "anode_type")
	@Enumerated(EnumType.STRING)
	@ColumnTransformer(write = "CAST(? AS usage.anode_type)", read = "anode_type::TEXT")
	private AnodeType anodeType;

	@Column
	private String ref;

	@Column(name = "start_effective_date")
	private Long startEffectiveDate; //in epoch-seconds

	@Column(name = "end_effective_date")
	private Long endEffectiveDate; //in epoch-seconds

	//TODO: double check this mapping
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pnode_id")
	private PnodeRef pnodeRef;
}
