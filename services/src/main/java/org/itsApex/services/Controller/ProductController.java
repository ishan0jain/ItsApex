package org.itsApex.services.Controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.Product;
import org.itsApex.services.Dao.ProductImage;
import org.itsApex.services.Dao.ShopDTO;
import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.ProductRepo;
import org.itsApex.services.Repository.ShopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ProductController {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ShopRepo shopRepo;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> listProducts(@RequestParam(name = "shopId", required = false) Integer shopId,
			HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
		if (shopId == null) {
			return ResponseEntity.ok(productRepo.findByShopOwnerUserId(user.getUserId()));
		}
		Optional<ShopDTO> shop = shopRepo.findById(shopId);
		if (shop.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		if (!isShopOwner(user, shop.get())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(productRepo.findByShopId(shopId));
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable("productId") Integer productId,
			HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
		Optional<Product> product = productRepo.findById(productId);
		if (product.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		ShopDTO shop = product.get().getShop();
		if (shop == null && product.get().getShopId() != null) {
			shop = shopRepo.findById(product.get().getShopId()).orElse(null);
		}
		if (shop == null) {
			return ResponseEntity.notFound().build();
		}
		if (!isShopOwner(user, shop)) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(product.get());
	}

	@GetMapping("/products/public")
	public List<Product> listPublicProducts() {
		return productRepo.findByActiveTrue();
	}

	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product, HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
		if (product.getShop() == null && product.getShopId() != null) {
			ShopDTO shop = shopRepo.findById(product.getShopId()).orElse(null);
			if (shop == null) {
				return ResponseEntity.badRequest().build();
			}
			if (!isShopOwner(user, shop)) {
				return ResponseEntity.status(403).build();
			}
			product.setShop(shop);
		}
		normalizeProductQuantities(product);
		if (product.getCurrency() == null || product.getCurrency().isBlank()) {
			product.setCurrency("INR");
		}
		if (product.getCreatedTs() == null) {
			product.setCreatedTs(Instant.now());
		}
		product.setActive(true);
		return ResponseEntity.ok(productRepo.save(product));
	}

	@PostMapping("/products/{productId}/images")
	public ResponseEntity<ProductImage> uploadProductImage(@PathVariable("productId") Integer productId,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws java.io.IOException {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
		Optional<Product> product = productRepo.findById(productId);
		if (product.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Product entity = product.get();
		ShopDTO shop = entity.getShop();
		if (shop == null && entity.getShopId() != null) {
			shop = shopRepo.findById(entity.getShopId()).orElse(null);
		}
		if (shop == null) {
			return ResponseEntity.notFound().build();
		}
		if (!isShopOwner(user, shop)) {
			return ResponseEntity.status(403).build();
		}
		ProductImage image = new ProductImage();
		image.setProduct(entity);
		image.setImage(file.getBytes());
		image.setContentType(file.getContentType());
		image.setFileName(file.getOriginalFilename());
		entity.getProductImages().add(image);
		productRepo.save(entity);
		return ResponseEntity.ok(image);
	}

	@GetMapping("/products/{productId}/images/{imageId}")
	public ResponseEntity<byte[]> getProductImage(@PathVariable("productId") Integer productId,
			@PathVariable("imageId") Integer imageId) {
		Optional<Product> product = productRepo.findById(productId);
		if (product.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return product.get().getProductImages().stream()
				.filter(img -> img.getImageId().equals(imageId))
				.findFirst()
				.map(img -> ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_TYPE, img.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : img.getContentType())
						.body(img.getImage()))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PatchMapping("/products/{productId}/inventory")
	public ResponseEntity<Product> updateInventory(@PathVariable("productId") Integer productId,
			@RequestParam("quantityAvailable") Integer quantityAvailable,
			HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
		Optional<Product> product = productRepo.findById(productId);
		if (product.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Product entity = product.get();
		ShopDTO shop = entity.getShop();
		if (shop == null && entity.getShopId() != null) {
			shop = shopRepo.findById(entity.getShopId()).orElse(null);
		}
		if (shop == null) {
			return ResponseEntity.notFound().build();
		}
		if (!isShopOwner(user, shop)) {
			return ResponseEntity.status(403).build();
		}
		entity.setQuantityAvailable(quantityAvailable);
		if (quantityAvailable != null) {
			entity.setStockQuantity(java.math.BigDecimal.valueOf(quantityAvailable));
			if (entity.getStockUnit() == null || entity.getStockUnit().isBlank()) {
				entity.setStockUnit(entity.getSellUnit() == null || entity.getSellUnit().isBlank() ? "number" : entity.getSellUnit());
			}
		}
		return ResponseEntity.ok(productRepo.save(entity));
	}

	@PatchMapping("/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
			@RequestBody Product update,
			HttpServletRequest request) {
		UserDTO user = getSessionUser(request);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
		Optional<Product> product = productRepo.findById(productId);
		if (product.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Product entity = product.get();
		ShopDTO shop = entity.getShop();
		if (shop == null && entity.getShopId() != null) {
			shop = shopRepo.findById(entity.getShopId()).orElse(null);
		}
		if (shop == null) {
			return ResponseEntity.notFound().build();
		}
		if (!isShopOwner(user, shop)) {
			return ResponseEntity.status(403).build();
		}
		if (update.getName() != null) {
			entity.setName(update.getName());
		}
		if (update.getDescription() != null) {
			entity.setDescription(update.getDescription());
		}
		if (update.getCategory() != null) {
			entity.setCategory(update.getCategory());
		}
		if (update.getTags() != null) {
			entity.setTags(update.getTags());
		}
		if (update.getPrice() != null) {
			entity.setPrice(update.getPrice());
		}
		if (update.getCurrency() != null) {
			entity.setCurrency(update.getCurrency());
		}
		if (update.getQuantityAvailable() != null) {
			entity.setQuantityAvailable(update.getQuantityAvailable());
		}
		if (update.getStockQuantity() != null) {
			entity.setStockQuantity(update.getStockQuantity());
		}
		if (update.getStockUnit() != null) {
			entity.setStockUnit(update.getStockUnit());
		}
		if (update.getSellQuantity() != null) {
			entity.setSellQuantity(update.getSellQuantity());
		}
		if (update.getSellUnit() != null) {
			entity.setSellUnit(update.getSellUnit());
		}
		if (update.getImageUrl() != null) {
			entity.setImageUrl(update.getImageUrl());
		}
		if (update.getActive() != null) {
			entity.setActive(update.getActive());
		}
		normalizeProductQuantities(entity);
		return ResponseEntity.ok(productRepo.save(entity));
	}

	private UserDTO getSessionUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (UserDTO) session.getAttribute("user");
	}

	private boolean isShopOwner(UserDTO user, ShopDTO shop) {
		return user != null && shop != null && shop.getOwnerUserId() != null
				&& shop.getOwnerUserId().equals(user.getUserId());
	}

	private void normalizeProductQuantities(Product product) {
		if (product == null) {
			return;
		}
		if (product.getSellQuantity() == null || product.getSellQuantity().compareTo(java.math.BigDecimal.ZERO) <= 0) {
			product.setSellQuantity(java.math.BigDecimal.ONE);
		}
		if (product.getSellUnit() == null || product.getSellUnit().isBlank()) {
			if (product.getStockUnit() != null && !product.getStockUnit().isBlank()) {
				product.setSellUnit(product.getStockUnit());
			} else {
				product.setSellUnit("number");
			}
		}
		if (product.getStockQuantity() == null && product.getQuantityAvailable() != null) {
			product.setStockQuantity(java.math.BigDecimal.valueOf(product.getQuantityAvailable()));
		}
		if (product.getStockUnit() == null || product.getStockUnit().isBlank()) {
			product.setStockUnit(product.getSellUnit());
		}
	}
}
