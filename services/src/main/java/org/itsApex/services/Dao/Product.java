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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
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
	@Column(name = "stock_quantity")
	BigDecimal stockQuantity;
	@Column(name = "stock_unit")
	String stockUnit;
	@Column(name = "sell_quantity")
	BigDecimal sellQuantity;
	@Column(name = "sell_unit")
	String sellUnit;
	String imageUrl;
	Boolean active;
	@Column(name = "created_ts")
	Instant createdTs;

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@com.fasterxml.jackson.annotation.JsonManagedReference("product-images")
	java.util.List<ProductImage> productImages = new java.util.ArrayList<>();
}
