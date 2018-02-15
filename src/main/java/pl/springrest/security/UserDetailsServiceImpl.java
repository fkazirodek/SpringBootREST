package pl.springrest.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.springrest.domain.user.UserRole;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.UserDTO;

public class UserDetailsServiceImpl implements UserDetailsService {

	private UserService userService;

	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO userDTO = userService.getUserBy(username);
		if (userDTO == null) 
			throw new UsernameNotFoundException("username not found in database");
		
		String[] roles = userDTO.getRoles()
								.stream()
								.map(UserRole::getRole)
								.toArray(String[]::new);
		
		User userDetails = new User(userDTO.getLogin(), userDTO.getPassword(), AuthorityUtils.createAuthorityList(roles));
		return userDetails;
	}

}
