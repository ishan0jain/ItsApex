package org.itsApex.services.Dao;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dba_delivery_task")
@Getter
@Setter
public class DeliveryTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer taskId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	OrderHeader order;

	@Column(name = "order_id", insertable = false, updatable = false)
	Integer orderId;

	@ManyToOne
	@JoinColumn(name = "agent_id")
	DeliveryAgent agent;

	@Column(name = "agent_id", insertable = false, updatable = false)
	Integer agentId;

	String status;
	Double distanceKm;
	Integer estimatedMinutes;
	BigDecimal payoutAmount;
	@Column(name = "assigned_ts")
	Instant assignedTs;
}
