package org.itsApex.services.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.itsApex.services.Dao.ShopDTO;
import org.itsApex.services.Dao.ShopImage;
import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.ShopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ShopController {
	
	@Autowired
	ShopRepo shopRepo;

	@GetMapping("/shops")
	@ResponseBody
	public List<ShopDTO> getAllShops() {
		return shopRepo.findAll();
	}

	@GetMapping("/shops/mine")
	@ResponseBody
	public ResponseEntity<List<ShopDTO>> getMyShops(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(shopRepo.findByOwnerUserId(user.getUserId()));
	}
	
	@GetMapping("/shops/{shopId}")
	@ResponseBody
	public ResponseEntity<ShopDTO> getShop(@PathVariable("shopId") Integer shopId) {
		Optional<ShopDTO> shop = shopRepo.findById(shopId);
		return shop.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("/shops")
	@ResponseBody
	public ShopDTO registerShop(@RequestBody ShopDTO shopDto, HttpServletRequest request) {
		if (shopDto.getVerified() == null) {
			shopDto.setVerified(false);
		}
		HttpSession session = request.getSession(false);
		UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
		if (user != null) {
			shopDto.setOwnerUserId(user.getUserId());
		}
		return shopRepo.save(shopDto);
	}
	
	@PostMapping("/shops/{shopId}/images")
	@ResponseBody
	public ResponseEntity<ShopImage> uploadImage(@PathVariable("shopId") Integer shopId, @RequestParam("file") MultipartFile file) throws IOException {
		Optional<ShopDTO> shop = shopRepo.findById(shopId);
		if (shop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		ShopImage image = new ShopImage();
		image.setShop(shop.get());
		image.setImage(file.getBytes());
		image.setContentType(file.getContentType());
		image.setFileName(file.getOriginalFilename());
		shop.get().getShopImages().add(image);
		shopRepo.save(shop.get());
		return ResponseEntity.ok(image);
	}
	
	@GetMapping("/shops/{shopId}/images/{imageId}")
	public ResponseEntity<byte[]> getImage(@PathVariable("shopId") Integer shopId, @PathVariable("imageId") Integer imageId) {
		Optional<ShopDTO> shop = shopRepo.findById(shopId);
		if (shop.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return shop.get().getShopImages().stream()
				.filter(img -> img.getImageId().equals(imageId))
				.findFirst()
				.map(img -> ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_TYPE, img.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : img.getContentType())
						.body(img.getImage()))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/listOfAllShops")
	@ResponseBody
	public List<ShopDTO> listOfAllShops() {
		return shopRepo.findAll();
	}
	
	@PostMapping("/registerShop")
	@ResponseBody
	public ShopDTO registerShopLegacy(@RequestBody ShopDTO shopDto, HttpServletRequest request) {
		return registerShop(shopDto, request);
	}
	
	@PostMapping("/uploadImage")
	@ResponseBody
	public ResponseEntity<ShopImage> uploadImageLegacy(@RequestParam("file") MultipartFile file, @RequestParam("imageData") String shopId) throws IOException {
		Integer parsedShopId = Integer.parseInt(shopId);
		return uploadImage(parsedShopId, file);
	}
} 
