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

	// TODO Potential Embedded private DateTimeInterval interval;
	// TODO add UsagePoint reference once entity is available

	@Column(name = "hashed_id")
	private String hashedId;

	@Column(name = "last_update")
	private DateTimeInterval lastUpdate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "application_information_id", nullable = false)
	private ApplicationInformation applicationInformation;

	@OneToOne(optional = false)
	@JoinColumn (name = "authorization_id")
	private Authorization authorizationId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "retail_customer_id", nullable = false, insertable = false, updatable = false)
	private RetailCustomer retailCustomerId;

	@Column(name = "usage_point_id")
	private int usagePointId;
}
