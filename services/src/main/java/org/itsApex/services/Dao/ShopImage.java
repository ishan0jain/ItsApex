package org.itsApex.services.Dao;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dba_shop_image")
@Getter
@Setter
public class ShopImage {
	@Id
	Integer imageId;
	@Lob
	byte[] image;
	@Column(name="shop_id", insertable=false, updatable=false)
	Integer shopId;
	
	
	
	@ManyToOne
	@JoinColumn(name="shop_id")
	@JsonBackReference
	ShopDTO shop;
}
