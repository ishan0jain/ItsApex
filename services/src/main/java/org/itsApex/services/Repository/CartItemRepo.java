package org.itsApex.services.Repository;

import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
	Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);
	long countByCartId(Integer cartId);
	List<CartItem> findByCartId(Integer cartId);
}
