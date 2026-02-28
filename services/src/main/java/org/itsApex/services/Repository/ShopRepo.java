package org.itsApex.services.Repository;

import org.itsApex.services.Dao.ShopDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepo extends JpaRepository<ShopDTO,Integer>{
	java.util.List<ShopDTO> findByOwnerUserId(Integer ownerUserId);
}
