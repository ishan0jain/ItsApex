package org.itsApex.services.Dao;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="dba_user_role")
@Data
@NoArgsConstructor // Generates a no-args constructor.
@AllArgsConstructor // Generates a constructor with all arguments.
@Builder
@Getter
@Setter
public class UserRole {
	@Id
	Integer userRoleId;
	String roleCd;
	String latitude;
	String longitude;
	
	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer userId;
	
	@ManyToOne
	@JoinColumn(name="user_id",referencedColumnName="userId")
	@JsonBackReference
	UserDTO user;
}
