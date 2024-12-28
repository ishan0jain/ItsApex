package org.itsApex.services.Controller;


import java.util.HashMap;
import java.util.Map;

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

   	
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            HttpSession session = request.getSession(true);
            session.setAttribute("user", "ishan");
            return ResponseEntity.ok().body(Map.of("message", "Login successful", "sessionId", session.getId()));
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
