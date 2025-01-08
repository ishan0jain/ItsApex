package org.itsApex.services.Controller;


import java.util.HashMap;
import java.util.Map;

import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@RestController
public class LoginController {

	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepo userRepo;

   	
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            UserDTO user = userRepo.findByUsrNm(authRequest.getUsername());
            
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            return ResponseEntity.ok().body(user);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Login Failed", "message", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out");
        return response;
    }
    
    @Getter
    static class AuthRequest {
        private String username;
        private String password;
        
        public String getUsername() {
        	return username;
        }
        public String getPassword() {
        	return password;
        }
        
        

        // Getters and Setters
    }
}
