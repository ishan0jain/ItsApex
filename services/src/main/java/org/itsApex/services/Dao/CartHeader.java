package org.itsApex.services.Dao;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dba_cart")
@Getter
@Setter
public class CartHeader {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer cartId;

	@Column(name = "user_id")
	Integer userId;

	@Column(name = "cart_name")
	String cartName;

	@Column(name = "default_cart")
	Boolean defaultCart;

	@Column(name = "created_ts")
	Instant createdTs;

	@Column(name = "updated_ts")
	Instant updatedTs;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("cart-items")
	List<CartItem> items = new ArrayList<>();
}
