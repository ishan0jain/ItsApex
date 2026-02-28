package org.itsApex.services.Dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dba_reg_field")
@Getter
@Setter
public class RegulationField {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer fieldId;
	String merchantType;
	String fieldKey;
	String label;
	String fieldType;
	Boolean required;
	String options;
	@Column(name = "display_order")
	Integer displayOrder;
	Boolean active;
}
