package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscription", schema = "usage") // the table name is plural?
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class Subscription extends IdentifiedObject {

	@Column(name = "hashed_id")
	private String hashedId;

	@Column(name = "last_update")
	private LocalDateTime lastUpdate;

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "application_information_id", nullable = false)
	private ApplicationInformation applicationInformation;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn (name = "authorization_id")
	private Authorization authorization;

	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "retail_customer_id", nullable = false)
	private RetailCustomer retailCustomer;

	// TODO add UsagePoint reference once entity is available
//	@Column(name = "usage_point_id")
//	private int usagePointId;
}
