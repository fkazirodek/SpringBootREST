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

	/**
	 * Find user in database and convert to DTO
	 * @param id user id
	 * @return UserDTO or null if user not exist
	 */
	public UserDTO getUserBy(long id) {
		User user = userRepository.findOne(id);
		if (user == null)
			return null;
		return userDTOConverter.convert(user);
	}
	
	/**
	 * Find user in database and convert to DTO
	 * @param id user login
	 * @return UserDTO or null if user not exist
	 */
	public UserDTO getUserBy(String login) {
		User user = userRepository.findByLogin(login);
		if (user == null)
			return null;
		return userDTOConverter.convert(user);
	}

	/**
	 * Save user in database if user not exist
	 * @param user User to save
	 * @return UserDTO or null if user already exist in database
	 */
	public UserDTO saveUser(User user) {
		if (userRepository.findByLogin(user.getLogin()) != null)
			return null;
		return userDTOConverter.convert(userRepository.save(user));
	}

	/**
	 * Delete User from database if user exist
	 * @param id User id
	 * @return true if user has been found and deleted or false if user not found in database
	 */
	public boolean deleteUser(long id) {
		User user = userRepository.findOne(id);
		if (user == null)
			return false;
		userRepository.delete(id);
		return true;

	}
}
