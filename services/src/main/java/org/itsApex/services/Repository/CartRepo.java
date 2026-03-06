package org.itsApex.services.Repository;

import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.CartHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<CartHeader, Integer> {
	List<CartHeader> findByUserIdOrderByCreatedTsAsc(Integer userId);
	Optional<CartHeader> findByUserIdAndDefaultCartTrue(Integer userId);
}
