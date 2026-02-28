package org.itsApex.services.Dao;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	String city;
	String state;
	String pinCode;
	String landmark;
	String shopType;
	String contactPhone;
	Boolean verified;
	Integer ownerUserId;
	
	@OneToMany(mappedBy="shop", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	List<ShopImage> shopImages = new ArrayList<>();
	
}
