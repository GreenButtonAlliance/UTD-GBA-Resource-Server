package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.greenbuttonalliance.gbaresourceserver.common.model.DateTimeInterval;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subsciption", schema = "usage") // the table name is plural?
@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@RequiredArgsConstructor
public class Subscription extends IdentifiedObject {

	@Embedded
	private DateTimeInterval interval;

	// TODO add UsagePoint reference once entity is available

	@Column(name = "hashed_id")
	private String hashedId;

	@Column(name = "last_update")
	private DateTimeInterval lastUpdate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "application_information", nullable = false)
	private ApplicationInformation applicationInformation; //TO _DO change teh id names

	@Column(name = "authorization_id")
	private Long authorization_id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "retail_customer", nullable = false)
	private RetailCustomer retail_customer;

	@Column(name = "usagepoint_id")
	private int usagepoint_id;
}
