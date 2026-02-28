package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
	List<OrderItem> findByOrderId(Integer orderId);
}
