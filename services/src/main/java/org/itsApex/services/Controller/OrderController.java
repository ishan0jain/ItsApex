package org.itsApex.services.Controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.OrderHeader;
import org.itsApex.services.Dao.OrderItem;
import org.itsApex.services.Dao.Product;
import org.itsApex.services.Dao.ShopDTO;
import org.itsApex.services.Repository.OrderRepo;
import org.itsApex.services.Repository.ProductRepo;
import org.itsApex.services.Repository.ShopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;

@RestController
public class OrderController {

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	ShopRepo shopRepo;

	@PostMapping("/orders")
	@Transactional
	public ResponseEntity<OrderHeader> createOrder(@RequestBody OrderRequest request) {
		Optional<ShopDTO> shop = shopRepo.findById(request.getShopId());
		if (shop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		OrderHeader order = new OrderHeader();
		order.setShop(shop.get());
		order.setBuyerName(request.getBuyerName());
		order.setBuyerPhone(request.getBuyerPhone());
		order.setDeliveryAddress(request.getDeliveryAddress());
		order.setDeliveryNotes(request.getDeliveryNotes());
		order.setStatus("NEW");
		order.setPaymentStatus("PENDING");
		order.setCreatedTs(Instant.now());

		BigDecimal total = BigDecimal.ZERO;
		List<OrderItem> items = new ArrayList<>();
		for (OrderItemRequest item : request.getItems()) {
			Optional<Product> product = productRepo.findById(item.getProductId());
			if (product.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			Product productEntity = product.get();
			int quantity = item.getQuantity() == null ? 1 : item.getQuantity();
			if (productEntity.getQuantityAvailable() != null && productEntity.getQuantityAvailable() < quantity) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(productEntity);
			orderItem.setQuantity(quantity);
			orderItem.setUnitPrice(productEntity.getPrice() == null ? BigDecimal.ZERO : productEntity.getPrice());
			orderItem.setCustomNote(item.getCustomNote());
			items.add(orderItem);
			BigDecimal price = productEntity.getPrice() == null ? BigDecimal.ZERO : productEntity.getPrice();
			total = total.add(price.multiply(BigDecimal.valueOf(quantity)));

			if (productEntity.getQuantityAvailable() != null) {
				productEntity.setQuantityAvailable(productEntity.getQuantityAvailable() - quantity);
				productRepo.save(productEntity);
			}
		}
		order.setItems(items);
		order.setTotalAmount(total);
		return ResponseEntity.ok(orderRepo.save(order));
	}

	@GetMapping("/orders")
	public List<OrderHeader> listOrders(@RequestParam(required = false) Integer shopId) {
		if (shopId == null) {
			return orderRepo.findAll();
		}
		return orderRepo.findByShopId(shopId);
	}

	@PatchMapping("/orders/{orderId}/status")
	public ResponseEntity<OrderHeader> updateStatus(@PathVariable Integer orderId, @RequestParam String status, @RequestParam(required = false) String paymentStatus) {
		Optional<OrderHeader> order = orderRepo.findById(orderId);
		if (order.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		OrderHeader entity = order.get();
		entity.setStatus(status);
		if (paymentStatus != null) {
			entity.setPaymentStatus(paymentStatus);
		}
		return ResponseEntity.ok(orderRepo.save(entity));
	}

	@Getter
	@Setter
	static class OrderRequest {
		Integer shopId;
		String buyerName;
		String buyerPhone;
		String deliveryAddress;
		String deliveryNotes;
		List<OrderItemRequest> items = new ArrayList<>();
	}

	@Getter
	@Setter
	static class OrderItemRequest {
		Integer productId;
		Integer quantity;
		String customNote;
	}
}
