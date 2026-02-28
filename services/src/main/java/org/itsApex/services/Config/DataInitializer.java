package org.itsApex.services.Config;

import java.time.Instant;
import java.util.List;

import org.itsApex.services.Dao.RegulationField;
import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Dao.UserRole;
import org.itsApex.services.Repository.RegulationFieldRepo;
import org.itsApex.services.Repository.UserRepo;
import org.itsApex.services.Repository.UserRoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final RegulationFieldRepo regulationFieldRepo;
	private final UserRepo userRepo;
	private final UserRoleRepo userRoleRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		if (regulationFieldRepo.count() == 0) {
			seedRegulationFields();
		}
		seedDefaultUserIfMissing();
	}

	private void seedRegulationFields() {
		List<RegulationField> fields = List.of(
				field("ALL", "businessRegId", "Business Registration ID", "text", true, null, 1),
				field("ALL", "contactEmail", "Contact Email", "email", true, null, 2),
				field("ALL", "taxId", "Tax ID", "text", true, null, 3),
				field("Restaurant", "foodSafetyCert", "Food Safety Certificate", "text", true, null, 10),
				field("Restaurant", "kitchenInspection", "Last Kitchen Inspection Date", "date", false, null, 11),
				field("Medical", "pharmacyLicense", "Pharmacy License Number", "text", true, null, 10),
				field("Grocery", "coldStorage", "Cold Storage Available", "select", true, "Yes,No", 10),
				field("Bakery", "allergenPolicy", "Allergen Policy", "textarea", false, null, 10)
		);
		regulationFieldRepo.saveAll(fields);
	}

	private RegulationField field(String merchantType, String key, String label, String type, boolean required, String options, int order) {
		RegulationField field = new RegulationField();
		field.setMerchantType(merchantType);
		field.setFieldKey(key);
		field.setLabel(label);
		field.setFieldType(type);
		field.setRequired(required);
		field.setOptions(options);
		field.setDisplayOrder(order);
		field.setActive(true);
		return field;
	}

	private void seedDefaultUserIfMissing() {
		UserDTO existing = userRepo.findByUsrNm("demo");
		if (existing != null) {
			return;
		}

		Integer nextUserId = 1;
		UserDTO lastUser = userRepo.findTopByOrderByUserIdDesc();
		if (lastUser != null && lastUser.getUserId() != null) {
			nextUserId = lastUser.getUserId() + 1;
		}

		UserDTO user = new UserDTO();
		user.setUserId(nextUserId);
		user.setUsrNm("demo");
		user.setFirstNm("Demo");
		user.setLastNm("User");
		user.setActvInd("Y");
		user.setLastLoginTs(java.util.Date.from(Instant.now()));
		user.setPassword(passwordEncoder.encode("demo123"));
		userRepo.save(user);

		Integer nextRoleId = 1;
		UserRole lastRole = userRoleRepo.findTopByOrderByUserRoleIdDesc();
		if (lastRole != null && lastRole.getUserRoleId() != null) {
			nextRoleId = lastRole.getUserRoleId() + 1;
		}
		UserRole role = new UserRole();
		role.setUserRoleId(nextRoleId);
		role.setRoleCd("C");
		role.setUser(user);
		userRoleRepo.save(role);
	}
}
