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
@Table(name = "dba_reg_answer")
@Getter
@Setter
public class RegulationAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer answerId;

	@Column(name = "shop_id")
	Integer shopId;

	String fieldKey;
	String fieldValue;
}
