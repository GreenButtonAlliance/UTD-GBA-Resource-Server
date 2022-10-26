package org.greenbuttonalliance.gbaresourceserver.usage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "line_item", schema = "usage")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class LineItem {

	@Id
	private UUID uuid;

	@Column
	@NonNull
	private Long amount;

	@Column(name = "date_time")
	@NonNull
	private LocalDateTime dateTime;

	@Column
	private String note;

	@Column
	private Long rounding;

	@ManyToOne(optional = false)
	@JoinColumn(name = "usage_summary_uuid", nullable = false)
	private UsageSummary usageSummary;
}
