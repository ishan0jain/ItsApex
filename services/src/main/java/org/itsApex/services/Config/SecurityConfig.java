package org.itsApex.services.Config;

import java.util.Arrays;

import org.apache.catalina.filters.CorsFilter;
import org.itsApex.services.userService.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	UserService userService;
	
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		 return http
//		            .csrf(csrf -> csrf.disable()) // Disable CSRF protection
//		            .authorizeHttpRequests(auth -> auth
//		            		.anyRequest().permitAll() // All other requests require authentication
//		            )
//		            .build();
//	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 return http
		            .csrf(csrf -> csrf.disable())// Disable CSRF protection
		            .cors(cors->cors.configurationSource(apiConfigurationSource()))
		            .authorizeHttpRequests(auth -> auth
		    		                .requestMatchers("/login").permitAll() // Allow public access to /login
		    	                    .anyRequest().access((authentication, context) -> {
		    	                        HttpServletRequest request = context.getRequest();
		    	                        HttpSession session = request.getSession(false); // Check if session exists
		    	                        if (session != null && !session.isNew()) { // Check if session is valid
		    	                            return new AuthorizationDecision(true); // Permit request if valid
		    	                        } else {
		    	                            return new AuthorizationDecision(false); // Deny request if no valid session
		    	                        }
		    	                    })
		    		            )

		            .build();
	}
	 @Bean
	 public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	 
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	               .authenticationProvider(authenticationProvider())
	               .build();
	}
	UrlBasedCorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


}
