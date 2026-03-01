package org.itsApex.services.Controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.itsApex.services.Dao.DeliveryAgent;
import org.itsApex.services.Dao.DeliveryTask;
import org.itsApex.services.Dao.OrderHeader;
import org.itsApex.services.Dao.ShopDTO;
import org.itsApex.services.Repository.DeliveryAgentRepo;
import org.itsApex.services.Repository.DeliveryTaskRepo;
import org.itsApex.services.Repository.OrderRepo;
import org.itsApex.services.util.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;

@RestController
public class DeliveryController {

	private static final BigDecimal BASE_PAYOUT = BigDecimal.valueOf(20);
	private static final BigDecimal PER_KM = BigDecimal.valueOf(5);
	private static final BigDecimal PER_MIN = BigDecimal.valueOf(1);

	@Autowired
	DeliveryAgentRepo agentRepo;

	@Autowired
	DeliveryTaskRepo taskRepo;

	@Autowired
	OrderRepo orderRepo;

	@PostMapping("/delivery/agents")
	public DeliveryAgent registerAgent(@RequestBody DeliveryAgent agent) {
		if (agent.getActive() == null) {
			agent.setActive(true);
		}
		return agentRepo.save(agent);
	}

	@GetMapping("/delivery/agents")
	public List<DeliveryAgent> listAgents() {
		return agentRepo.findAll();
	}

	@GetMapping("/delivery/tasks")
	public List<DeliveryTask> listTasks(@RequestParam(name = "agentId", required = false) Integer agentId, @RequestParam(name = "status", required = false) String status) {
		if (agentId != null) {
			return taskRepo.findByAgentId(agentId);
		}
		if (status != null) {
			return taskRepo.findByStatus(status);
		}
		return taskRepo.findAll();
	}

	@PostMapping("/delivery/tasks/assign")
	@Transactional
	public ResponseEntity<DeliveryTask> assignTask(@RequestBody DeliveryAssignmentRequest request) {
		Optional<OrderHeader> order = orderRepo.findById(request.getOrderId());
		if (order.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		OrderHeader orderEntity = order.get();
		ShopDTO shop = orderEntity.getShop();
		if (shop == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		DeliveryAgent agent = request.getAgentId() == null ? selectBestAgent(shop) : agentRepo.findById(request.getAgentId()).orElse(null);
		if (agent == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		double distance = estimateDistanceKm(agent, shop);
		int minutes = estimateMinutes(distance);
		BigDecimal payout = estimatePayout(distance, minutes);

		DeliveryTask task = new DeliveryTask();
		task.setOrder(orderEntity);
		task.setAgent(agent);
		task.setStatus("ASSIGNED");
		task.setDistanceKm(distance);
		task.setEstimatedMinutes(minutes);
		task.setPayoutAmount(payout);
		task.setAssignedTs(Instant.now());
		orderEntity.setStatus("ASSIGNED");
		orderRepo.save(orderEntity);
		return ResponseEntity.ok(taskRepo.save(task));
	}

	@PostMapping("/delivery/estimate")
	public DeliveryEstimateResponse estimate(@RequestBody DeliveryEstimateRequest request) {
		double distance = request.getDistanceKm() == null ? 0 : request.getDistanceKm();
		int minutes = request.getEstimatedMinutes() == null ? estimateMinutes(distance) : request.getEstimatedMinutes();
		BigDecimal payout = estimatePayout(distance, minutes);
		DeliveryEstimateResponse response = new DeliveryEstimateResponse();
		response.setDistanceKm(distance);
		response.setEstimatedMinutes(minutes);
		response.setPayoutAmount(payout);
		return response;
	}

	private DeliveryAgent selectBestAgent(ShopDTO shop) {
		List<DeliveryAgent> agents = agentRepo.findByActiveTrue();
		if (agents.isEmpty()) {
			return null;
		}
		Set<String> zones = normalizeZones(shop);
		return agents.stream()
				.filter(agent -> zones.isEmpty() || agentMatchesZone(agent, zones))
				.min(Comparator.comparingDouble(agent -> estimateDistanceKm(agent, shop)))
				.orElse(null);
	}

	private boolean agentMatchesZone(DeliveryAgent agent, Set<String> zones) {
		if (agent.getZones() == null || agent.getZones().isBlank()) {
			return true;
		}
		for (String zone : agent.getZones().split(",")) {
			if (zones.contains(zone.trim().toLowerCase(Locale.ROOT))) {
				return true;
			}
		}
		return false;
	}

	private Set<String> normalizeZones(ShopDTO shop) {
		return List.of(shop.getCity(), shop.getState(), shop.getPinCode()).stream()
				.filter(value -> value != null && !value.isBlank())
				.map(value -> value.trim().toLowerCase(Locale.ROOT))
				.collect(Collectors.toSet());
	}

	private double estimateDistanceKm(DeliveryAgent agent, ShopDTO shop) {
		try {
			double agentLat = Double.parseDouble(agent.getLatitude());
			double agentLon = Double.parseDouble(agent.getLongitude());
			double shopLat = Double.parseDouble(shop.getLatitude());
			double shopLon = Double.parseDouble(shop.getLongitude());
			return GeoUtils.distanceKm(agentLat, agentLon, shopLat, shopLon);
		} catch (Exception e) {
			return 0;
		}
	}

	private int estimateMinutes(double distanceKm) {
		double avgSpeed = 25.0;
		return (int) Math.ceil((distanceKm / avgSpeed) * 60.0);
	}

	private BigDecimal estimatePayout(double distanceKm, int minutes) {
		return BASE_PAYOUT.add(PER_KM.multiply(BigDecimal.valueOf(distanceKm))).add(PER_MIN.multiply(BigDecimal.valueOf(minutes)));
	}

	@Getter
	@Setter
	static class DeliveryAssignmentRequest {
		Integer orderId;
		Integer agentId;
	}

	@Getter
	@Setter
	static class DeliveryEstimateRequest {
		Double distanceKm;
		Integer estimatedMinutes;
	}

	@Getter
	@Setter
	static class DeliveryEstimateResponse {
		Double distanceKm;
		Integer estimatedMinutes;
		BigDecimal payoutAmount;
	}
}
