package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.RegulationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulationAnswerRepo extends JpaRepository<RegulationAnswer, Integer> {
	List<RegulationAnswer> findByShopId(Integer shopId);
}
