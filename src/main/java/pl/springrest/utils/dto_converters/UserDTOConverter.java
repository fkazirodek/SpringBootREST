package pl.springrest.utils.dto_converters;

import org.springframework.stereotype.Component;

import pl.springrest.domain.user.Address;
import pl.springrest.domain.user.User;
import pl.springrest.dto.UserDTO;

/**
 * This Class implements DTOConverter
 * 
 * @see DTOConventer
 */

@Component
public class UserDTOConverter implements DTOConverter<User, UserDTO> {

	@Override
	public UserDTO convert(User user) {
		UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPassword(), user.getEmail());
		Address address = user.getAddress();
		if (address != null)
			userDTO.setAddress(address);
		return userDTO;
	}
	
	public User convert(UserDTO userDto) {
		User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getLogin(), userDto.getPassword(), userDto.getEmail());
		Address address = userDto.getAddress();
		if (address != null)
			user.setAddress(address);
		return user;
	}
}
