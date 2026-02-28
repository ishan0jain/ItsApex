package org.itsApex.services.Dao;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dba_order")
@Getter
@Setter
public class OrderHeader {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer orderId;

	@ManyToOne
	@JoinColumn(name = "shop_id")
	ShopDTO shop;

	@Column(name = "shop_id", insertable = false, updatable = false)
	Integer shopId;

	String buyerName;
	String buyerPhone;
	String deliveryAddress;
	String deliveryNotes;
	String status;
	String paymentStatus;
	BigDecimal totalAmount;
	@Column(name = "created_ts")
	Instant createdTs;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	List<OrderItem> items = new ArrayList<>();
}
