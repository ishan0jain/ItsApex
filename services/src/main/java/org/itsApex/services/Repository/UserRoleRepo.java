package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRole,Integer>{
	List<UserRole> findByUserId(Integer userId);
}