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
import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {
	
	@Autowired
	UserRepo userRepo;
	
	@GetMapping("/home")	
	public String home(HttpServletRequest request)  {
		HttpSession session = request.getSession(true);
		System.out.println(session.getAttribute("user"));
		return "a";
	}
	@PostMapping("/getUserDTO")
	@ResponseBody
	public UserDTO getUserDetaile(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		UserDTO user = (UserDTO) session.getAttribute("user");
		return user;
	}
	

}
