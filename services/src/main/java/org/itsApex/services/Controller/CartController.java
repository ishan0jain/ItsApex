package org.itsApex.services.Controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.CartHeader;
import org.itsApex.services.Dao.CartItem;
import org.itsApex.services.Dao.Product;
import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.CartItemRepo;
import org.itsApex.services.Repository.CartRepo;
import org.itsApex.services.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	CartRepo cartRepo;

	@Autowired
	CartItemRepo cartItemRepo;

	@Autowired
	ProductRepo productRepo;

	@GetMapping
	public ResponseEntity<List<CartSummary>> listCarts(HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
		ensureDefaultCart(user.getUserId());
		List<CartHeader> carts = cartRepo.findByUserIdOrderByCreatedTsAsc(user.getUserId());
		List<CartSummary> summaries = new ArrayList<>();
		for (CartHeader cart : carts) {
			summaries.add(toSummary(cart));
		}
		return ResponseEntity.ok(summaries);
	}

	@PostMapping
	public ResponseEntity<CartSummary> createCart(@RequestBody CreateCartRequest requestBody, HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String name = requestBody == null ? null : requestBody.getName();
		if (name == null || name.trim().isEmpty()) {
			long count = cartRepo.findByUserIdOrderByCreatedTsAsc(user.getUserId()).size();
			name = "Cart " + (count + 1);
		}
		CartHeader cart = new CartHeader();
		cart.setUserId(user.getUserId());
		cart.setCartName(name.trim());
		cart.setDefaultCart(false);
		cart.setCreatedTs(Instant.now());
		cart.setUpdatedTs(Instant.now());
		return ResponseEntity.ok(toSummary(cartRepo.save(cart)));
	}

	@PostMapping("/items")
	public ResponseEntity<CartSummary> addItemToDefault(@RequestBody AddItemRequest requestBody, HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		CartHeader defaultCart = ensureDefaultCart(user.getUserId());
		return addItemToCart(defaultCart, requestBody);
	}

	@PostMapping("/{cartId}/items")
	public ResponseEntity<CartSummary> addItem(@PathVariable("cartId") Integer cartId, @RequestBody AddItemRequest requestBody, HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		Optional<CartHeader> cart = cartRepo.findById(cartId);
		if (cart.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		CartHeader cartEntity = cart.get();
		if (!user.getUserId().equals(cartEntity.getUserId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return addItemToCart(cartEntity, requestBody);
	}

	@GetMapping("/{cartId}")
	public ResponseEntity<CartDetail> getCart(@PathVariable("cartId") Integer cartId, HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		Optional<CartHeader> cart = cartRepo.findById(cartId);
		if (cart.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		CartHeader cartEntity = cart.get();
		if (!user.getUserId().equals(cartEntity.getUserId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		List<CartItem> items = cartItemRepo.findByCartId(cartId);
		CartDetail detail = new CartDetail();
		detail.setCartId(cartEntity.getCartId());
		detail.setCartName(cartEntity.getCartName());
		detail.setDefaultCart(Boolean.TRUE.equals(cartEntity.getDefaultCart()));
		List<CartItemView> views = new ArrayList<>();
		for (CartItem item : items) {
			CartItemView view = new CartItemView();
			view.setCartItemId(item.getCartItemId());
			view.setProductId(item.getProductId());
			view.setQuantity(item.getQuantity());
			view.setCustomNote(item.getCustomNote());
			Product product = item.getProduct();
			if (product != null) {
				view.setProductName(product.getName());
				view.setPrice(product.getPrice());
				view.setCurrency(product.getCurrency());
				view.setImageUrl(product.getImageUrl());
				view.setSellQuantity(product.getSellQuantity());
				view.setSellUnit(product.getSellUnit());
			}
			views.add(view);
		}
		detail.setItems(views);
		return ResponseEntity.ok(detail);
	}

	private ResponseEntity<CartSummary> addItemToCart(CartHeader cart, AddItemRequest requestBody) {
		if (requestBody == null || requestBody.getProductId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Optional<Product> product = productRepo.findById(requestBody.getProductId());
		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		int quantity = requestBody.getQuantity() == null || requestBody.getQuantity() < 1 ? 1 : requestBody.getQuantity();
		Optional<CartItem> existing = cartItemRepo.findByCartIdAndProductId(cart.getCartId(), product.get().getProductId());
		CartItem item = existing.orElseGet(CartItem::new);
		item.setCart(cart);
		item.setProduct(product.get());
		item.setQuantity(existing.isPresent() ? existing.get().getQuantity() + quantity : quantity);
		item.setCustomNote(requestBody.getCustomNote());
		Instant now = Instant.now();
		if (item.getCreatedTs() == null) {
			item.setCreatedTs(now);
		}
		item.setUpdatedTs(now);
		cartItemRepo.save(item);
		cart.setUpdatedTs(now);
		cartRepo.save(cart);
		return ResponseEntity.ok(toSummary(cart));
	}

	private CartSummary toSummary(CartHeader cart) {
		CartSummary summary = new CartSummary();
		summary.setCartId(cart.getCartId());
		summary.setCartName(cart.getCartName());
		summary.setDefaultCart(Boolean.TRUE.equals(cart.getDefaultCart()));
		summary.setItemCount(cartItemRepo.countByCartId(cart.getCartId()));
		return summary;
	}

	private CartHeader ensureDefaultCart(Integer userId) {
		Optional<CartHeader> existing = cartRepo.findByUserIdAndDefaultCartTrue(userId);
		if (existing.isPresent()) {
			return existing.get();
		}
		CartHeader cart = new CartHeader();
		cart.setUserId(userId);
		cart.setCartName("Default Cart");
		cart.setDefaultCart(true);
		Instant now = Instant.now();
		cart.setCreatedTs(now);
		cart.setUpdatedTs(now);
		return cartRepo.save(cart);
	}

	private UserDTO getSessionUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (UserDTO) session.getAttribute("user");
	}

	@Getter
	@Setter
	static class CreateCartRequest {
		String name;
	}

	@Getter
	@Setter
	static class AddItemRequest {
		Integer productId;
		Integer quantity;
		String customNote;
	}

	@Getter
	@Setter
	static class CartSummary {
		Integer cartId;
		String cartName;
		boolean defaultCart;
		long itemCount;
	}

	@Getter
	@Setter
	static class CartDetail {
		Integer cartId;
		String cartName;
		boolean defaultCart;
		List<CartItemView> items = new ArrayList<>();
	}

	@Getter
	@Setter
	static class CartItemView {
		Integer cartItemId;
		Integer productId;
		Integer quantity;
		String customNote;
		String productName;
		java.math.BigDecimal price;
		String currency;
		String imageUrl;
		java.math.BigDecimal sellQuantity;
		String sellUnit;
	}
}
