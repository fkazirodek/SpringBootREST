package pl.springrest.domain.user;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import pl.springrest.dto.UserDTO;
import pl.springrest.utils.dto_converters.UserDTOConverter;

@Service
@Transactional
public class UserService {

	private UserRepository userRepository;
	private UserDTOConverter userDTOConverter;

	public UserService(UserRepository userRepository, UserDTOConverter userDTOConverter) {
		this.userRepository = userRepository;
		this.userDTOConverter = userDTOConverter;
	}
	
	/**
	 * Return User entity, use only in service layer
	 * @param id user id
	 * @return User entity
	 */
	public User getUserEntity(long id) {
		return userRepository.findOne(id);	
	}

	/**
	 * Find user in database and convert to DTO
	 * 
	 * @param id
	 *            user id
	 * @return UserDTO
	 * @throws ResourceNotFoundException
	 *             if user not found
	 */
	public UserDTO getUserBy(long id) throws ResourceNotFoundException {
		User user = userRepository.findOne(id);
		if (user == null)
			throw new ResourceNotFoundException("User " + id + " not found");
		return userDTOConverter.convert(user);
	}

	/**
	 * Find user in database and convert to DTO
	 * 
	 * @param id
	 *            user login
	 * @return UserDTO
	 * @throws ResourceNotFoundException
	 *             if user not found
	 */
	public UserDTO getUserBy(String login) throws ResourceNotFoundException {
		User user = userRepository
						.findByLogin(login)
							.orElseThrow(ResourceNotFoundException::new);
		return userDTOConverter.convert(user);
	}

	/**
	 * Save user in database if user not exist
	 * 
	 * @param user
	 *            User to save
	 * @return UserDTO
	 * @throws DataIntegrityViolationException
	 *             if user already exist in database
	 */
	public UserDTO saveUser(UserDTO userDto) throws DuplicateKeyException {
		User savedUser;
		try {
			savedUser = userRepository.save(userDTOConverter.convert(userDto));
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateKeyException("User " + userDto.getLogin() + " exist");
		}
		return userDTOConverter.convert(savedUser);
	}

	/**
	 * Update user's address
	 * 
	 * @param address
	 *            Address that will be added to the user
	 * @param id
	 *            ID of the user to whom the address will be add
	 * @return return updated UserDTO when address was update successfully and addres is
	 *         not null
	 * @throws ResourceNotFoundException
	 *             when user not found in DB
	 */
	public UserDTO updateAddress(Address address, long id) throws ResourceNotFoundException {
		User dbUser = userRepository.findOne(id);
		if (dbUser == null)
			throw new ResourceNotFoundException("Can not update address because User " + id + " not found");
		if (address != null)
			dbUser.setAddress(address);
		return userDTOConverter.convert(dbUser);
	}

	/**
	 * Delete User from database if user exist
	 * 
	 * @param id
	 *            User id
	 * @throws ResourceNotFoundException
	 *             if user not found in database
	 */
	public void deleteUser(long id) throws ResourceNotFoundException {
		User user = userRepository.findOne(id);
		if (user == null)
			throw new ResourceNotFoundException("Can not delete user because user not found");
		userRepository.delete(id);

	}
}
