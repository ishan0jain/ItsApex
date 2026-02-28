package org.itsApex.services.userService;

import java.util.List;
import java.util.stream.Collectors;

import org.itsApex.services.Dao.UserDTO;
import org.itsApex.services.Dao.UserRole;
import org.itsApex.services.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO user = userRepo.findByUsrNm(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}
		List<SimpleGrantedAuthority> authorities = user.getUserRoles() == null
				? List.of()
				: user.getUserRoles().stream()
						.map(UserRole::getRoleCd)
						.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
						.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(
				user.getUsrNm(),
				user.getPassword(),
				authorities
		);
	}
}
