package org.itsApex.services.Dao;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "dba_order_item")
@Getter
@Setter
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer orderItemId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonBackReference
	OrderHeader order;

	@Column(name = "order_id", insertable = false, updatable = false)
	Integer orderId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;

	@Column(name = "product_id", insertable = false, updatable = false)
	Integer productId;

	Integer quantity;
	BigDecimal unitPrice;
	String customNote;
}
