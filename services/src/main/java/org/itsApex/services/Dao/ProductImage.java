package org.itsApex.services.Dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dba_product_image")
@Getter
@Setter
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer imageId;

	@Lob
	@Column(columnDefinition = "bytea")
	@JsonIgnore
	byte[] image;

	String contentType;
	String fileName;

	@Column(name = "product_id", insertable = false, updatable = false)
	Integer productId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference("product-images")
	Product product;
}
