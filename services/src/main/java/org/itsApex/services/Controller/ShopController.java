package org.itsApex.services.Controller;

import java.io.IOException;
import java.util.List;

import org.itsApex.services.Dao.ShopDTO;
import org.itsApex.services.Dao.ShopImage;
import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.ShopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/listOfAllShops")
	@ResponseBody
	public ShopDTO getAllShops(HttpServletRequest req) throws Exception{
		HttpSession ses = req.getSession();
		UserDTO user= (UserDTO) ses.getAttribute("user");
		if(user!=null && 
				!user.getUserRoles().isEmpty()) {
			List<ShopDTO> shops = shopRepo.findAll();
			return shops.get(0);
		}
		else 
			throw new Exception("Not Allowed");
	}
	
	
	@PostMapping("/registerShop")
	@ResponseBody
	public Integer registerShop(@RequestBody ShopDTO shopDto) {
		return shopDto.getShopId();
	}
	
	@PostMapping("/uploadImage")
	@ResponseBody
	public byte[] uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("imageData") String shopId) throws IOException {
		return file.getBytes();
	}
} 
