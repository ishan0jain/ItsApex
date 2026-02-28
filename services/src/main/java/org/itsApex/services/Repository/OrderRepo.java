package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<OrderHeader, Integer> {
	List<OrderHeader> findByShopId(Integer shopId);
}
