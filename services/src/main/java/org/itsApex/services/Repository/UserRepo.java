package org.itsApex.services.Repository;

import org.itsApex.services.Dao.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserDTO,Integer>{
	UserDTO findByUsrNm(String usrNm);
}
