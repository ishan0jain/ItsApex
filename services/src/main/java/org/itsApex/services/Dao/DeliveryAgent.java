package org.itsApex.services.Dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dba_delivery_agent")
@Getter
@Setter
public class DeliveryAgent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer agentId;
	String name;
	String phone;
	String zones;
	String latitude;
	String longitude;
	Boolean active;
}
