package org.itsApex.services.Controller;

import java.util.ArrayList;
import java.util.List;

import org.itsApex.services.Dao.RegulationAnswer;
import org.itsApex.services.Dao.RegulationField;
import org.itsApex.services.Repository.RegulationAnswerRepo;
import org.itsApex.services.Repository.RegulationFieldRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;

@RestController
public class RegistrationController {

	@Autowired
	RegulationFieldRepo regulationFieldRepo;
	
	@Autowired
	RegulationAnswerRepo regulationAnswerRepo;

	@GetMapping("/registration/fields")
	public List<RegulationField> getFields(@RequestParam(name = "merchantType", required = false) String merchantType) {
		List<RegulationField> fields = new ArrayList<>();
		fields.addAll(regulationFieldRepo.findByMerchantTypeIgnoreCaseAndActiveTrueOrderByDisplayOrderAsc("ALL"));
		if (merchantType != null && !merchantType.isBlank()) {
			fields.addAll(regulationFieldRepo.findByMerchantTypeIgnoreCaseAndActiveTrueOrderByDisplayOrderAsc(merchantType));
		}
		if (fields.isEmpty()) {
			return defaultFields(merchantType);
		}
		return fields;
	}

	private List<RegulationField> defaultFields(String merchantType) {
		List<RegulationField> fields = new ArrayList<>();
		fields.add(build("ALL", "businessRegId", "Business Registration ID", "text", true, 1));
		fields.add(build("ALL", "contactEmail", "Contact Email", "email", true, 2));
		fields.add(build("ALL", "taxId", "Tax ID", "text", true, 3));
		if (merchantType != null) {
			fields.add(build(merchantType, "licenseNumber", "License Number", "text", false, 10));
		}
		return fields;
	}

	private RegulationField build(String merchantType, String key, String label, String type, boolean required, int order) {
		RegulationField field = new RegulationField();
		field.setMerchantType(merchantType);
		field.setFieldKey(key);
		field.setLabel(label);
		field.setFieldType(type);
		field.setRequired(required);
		field.setDisplayOrder(order);
		field.setActive(true);
		return field;
	}

	@PostMapping("/registration/answers")
	public List<RegulationAnswer> saveAnswers(@RequestBody RegulationAnswerRequest request) {
		List<RegulationAnswer> answers = new ArrayList<>();
		if (request.getAnswers() == null) {
			return answers;
		}
		for (RegulationAnswerRequest.AnswerEntry entry : request.getAnswers()) {
			RegulationAnswer answer = new RegulationAnswer();
			answer.setShopId(request.getShopId());
			answer.setFieldKey(entry.getFieldKey());
			answer.setFieldValue(entry.getFieldValue());
			answers.add(answer);
		}
		return regulationAnswerRepo.saveAll(answers);
	}

	@Getter
	@Setter
	static class RegulationAnswerRequest {
		Integer shopId;
		List<AnswerEntry> answers = new ArrayList<>();

		@Getter
		@Setter
		static class AnswerEntry {
			String fieldKey;
			String fieldValue;
		}
	}
}
