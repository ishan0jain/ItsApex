package org.itsApex.services.Controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.Product;
import org.itsApex.services.Dao.ShopDTO;
import org.itsApex.services.Repository.ProductRepo;
import org.itsApex.services.Repository.ShopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ShopRepo shopRepo;

	@GetMapping("/products")
	public List<Product> listProducts(@RequestParam(required = false) Integer shopId) {
		if (shopId == null) {
			return productRepo.findAll();
		}
		return productRepo.findByShopId(shopId);
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
		Optional<Product> product = productRepo.findById(productId);
		return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		if (product.getShop() == null && product.getShopId() != null) {
			ShopDTO shop = shopRepo.findById(product.getShopId()).orElse(null);
			if (shop == null) {
				return ResponseEntity.badRequest().build();
			}
			product.setShop(shop);
		}
		if (product.getCreatedTs() == null) {
			product.setCreatedTs(Instant.now());
		}
		if (product.getActive() == null) {
			product.setActive(true);
		}
		return ResponseEntity.ok(productRepo.save(product));
	}

	@PatchMapping("/products/{productId}/inventory")
	public ResponseEntity<Product> updateInventory(@PathVariable Integer productId, @RequestParam Integer quantityAvailable) {
		Optional<Product> product = productRepo.findById(productId);
		if (product.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Product entity = product.get();
		entity.setQuantityAvailable(quantityAvailable);
		return ResponseEntity.ok(productRepo.save(entity));
	}
}
