package org.itsApex.services.Dao;


import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="dba_user")
@Data
@NoArgsConstructor // Generates a no-args constructor.
@AllArgsConstructor // Generates a constructor with all arguments.
@Builder
@Getter
@Setter
public class UserDTO {
	@Id
	Integer userId;
	
	@Column(unique = true)
	String usrNm;
	String firstNm;
	String lastNm;
	Date lastLoginTs;
	String actvInd;
	String password;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	List<UserRole>userRoles;



}
