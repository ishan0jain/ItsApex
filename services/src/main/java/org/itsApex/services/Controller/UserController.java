package org.itsApex.services.Controller;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {
	
	@Autowired
	UserRepo userRepo;
	
	@GetMapping("/home")	
	public String home(HttpServletRequest request)  {
		return "a";
	}
	@PostMapping("/getUserDTO")
	@ResponseBody
	public UserDTO getUserDetaile(@RequestParam("userName") String userName) {
		UserDTO user=  userRepo.findByUsrNm(userName);
		return user;
	}
	

}
