package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.DeliveryTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTaskRepo extends JpaRepository<DeliveryTask, Integer> {
	List<DeliveryTask> findByAgentId(Integer agentId);
	List<DeliveryTask> findByStatus(String status);
}
