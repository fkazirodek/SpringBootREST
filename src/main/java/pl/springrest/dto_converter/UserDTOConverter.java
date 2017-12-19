package pl.springrest.dto_converter;

import org.springframework.stereotype.Component;

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
		return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getEmail());
	}
}
