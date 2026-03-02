package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	List<Product> findByShopId(Integer shopId);
	List<Product> findByShopOwnerUserId(Integer ownerUserId);
	List<Product> findByActiveTrue();
}
