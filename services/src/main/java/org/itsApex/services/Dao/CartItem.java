package org.itsApex.services.Dao;

import java.time.Instant;

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
@Table(name = "dba_cart_item")
@Getter
@Setter
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer cartItemId;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	@JsonBackReference("cart-items")
	CartHeader cart;

	@Column(name = "cart_id", insertable = false, updatable = false)
	Integer cartId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;

	@Column(name = "product_id", insertable = false, updatable = false)
	Integer productId;

	Integer quantity;
	String customNote;

	@Column(name = "created_ts")
	Instant createdTs;

	@Column(name = "updated_ts")
	Instant updatedTs;
}
