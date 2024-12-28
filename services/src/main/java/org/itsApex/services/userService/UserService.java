package org.itsApex.services.userService;

import java.util.Collections;
import java.util.List;

import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new org.springframework.security.core.userdetails.User(
                "ishan",
                new BCryptPasswordEncoder().encode("w341324"),
                Collections.emptyList()
        );
	}
	
		

}
