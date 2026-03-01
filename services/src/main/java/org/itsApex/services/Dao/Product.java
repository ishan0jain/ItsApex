package org.itsApex.services.Dao;

import java.math.BigDecimal;
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
@Table(name = "dba_product")
@Getter
@Setter
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer productId;

	@ManyToOne
	@JoinColumn(name = "shop_id")
	@JsonBackReference
	ShopDTO shop;

	@Column(name = "shop_id", insertable = false, updatable = false)
	Integer shopId;

	String name;
	String description;
	String category;
	String tags;
	BigDecimal price;
	String currency;
	Integer quantityAvailable;
	String imageUrl;
	Boolean active;
	@Column(name = "created_ts")
	Instant createdTs;
}
