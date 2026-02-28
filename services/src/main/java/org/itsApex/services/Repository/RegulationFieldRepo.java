package org.itsApex.services.Repository;

import java.util.List;

import org.itsApex.services.Dao.RegulationField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulationFieldRepo extends JpaRepository<RegulationField, Integer> {
	List<RegulationField> findByMerchantTypeIgnoreCaseAndActiveTrueOrderByDisplayOrderAsc(String merchantType);
}
