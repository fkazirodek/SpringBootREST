package pl.springrest.domain.user;

import org.springframework.stereotype.Service;

import pl.springrest.dto.UserDTO;
import pl.springrest.dto_converter.UserDTOConverter;

@Service
public class UserService {

	private UserRepository userRepository;
	private UserDTOConverter userDTOConverter;

	public UserService(UserRepository userRepository, UserDTOConverter userDTOConverter) {
		this.userRepository = userRepository;
		this.userDTOConverter = userDTOConverter;
	}

	public UserDTO getUserByID(long id) {
		User user = userRepository.findOne(id);
		if (user == null)
			return null;
		return userDTOConverter.convert(user);
	}

	public UserDTO saveUser(User user) {
		if (userRepository.findByLogin(user.getLogin()) != null)
			return null;
		return userDTOConverter.convert(userRepository.save(user));
	}

	public boolean deleteUser(long id) {
		User user = userRepository.findOne(id);
		if (user == null)
			return false;
		userRepository.delete(id);
		return true;

	}
}
