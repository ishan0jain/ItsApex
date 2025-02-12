package org.itsApex.services.Dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dba_shop")
@Getter
@Setter
public class ShopDTO {
	
	@Id
	Integer shopId;
	String shopName;
	String description;
	String tags;
	String longitude;
	String latitude;
	Integer radius;
	String retaileFirstName;
	String retailerLastName;
	String address;
	
	@OneToMany(mappedBy="shop", fetch=FetchType.EAGER)
	@JsonManagedReference
	List<ShopImage> shopImages;
	
}
