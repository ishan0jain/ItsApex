package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAgentRepo extends JpaRepository<DeliveryAgent, Integer> {
	List<DeliveryAgent> findByActiveTrue();
}
