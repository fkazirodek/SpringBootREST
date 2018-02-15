package pl.springrest.domain.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import pl.springrest.domain.rating.Rating;
import pl.springrest.dto.UserDTO;
import pl.springrest.dto.UserListDTO;
import pl.springrest.utils.dto_converters.UserDTOConverter;

@Service
@Transactional
public class UserService {

	private static final String DEFAULT_ROLE = "ROLE_USER";
	
	private UserRepository userRepository;
	private UserDTOConverter userDTOConverter;
	private RoleRepository roleRepository;

	public UserService(UserRepository userRepository, UserDTOConverter userDTOConverter, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.userDTOConverter = userDTOConverter;
		this.roleRepository = roleRepository;
	}

	/**
	 * Get all users
	 * 
	 * @return List of UserDTO
	 */
	public UserListDTO getAllUsers() {
		List<UserDTO> userDTOs = userDTOConverter.convertAll(userRepository.findAll());
		return new UserListDTO(userDTOs);
	}
	
	/**
	 * Find user by login
	 * 
	 * @param login
	 * @return
	 * @throws ResourceNotFoundException
	 *             if user not found
	 */
	public UserDTO getUserBy(String login) {
		return userRepository
				.findByLogin(login)
				.map(userDTOConverter::convert)
				.orElseThrow(() -> new ResourceNotFoundException("User " + login + " not found" )); 
	}
	
	/**
	 * Find user by ID
	 * 
	 * @param id
	 * @return UserDTO
	 * @throws ResourceNotFoundException
	 *             if user not found
	 */
	public UserDTO getUserBy(long id) {
		return userRepository
				.findById(id)
				.map(userDTOConverter::convert)
				.orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
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
	public UserDTO saveUser(UserDTO userDto) {
		User user = roleRepository
					.findByRole(DEFAULT_ROLE)
					.map(role -> {
						userDto.addRole(role);
						return userDto;
					})
					.map(userDTOConverter::convert)
					.get();
			
		User savedUser;
		try {
			savedUser = userRepository.save(user);
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
	public UserDTO updateAddress(Address address, long id) {
		return userRepository
					.findById(id)
					.map(user -> {
						user.setAddress(address);
						return user;
					})
					.map(userDTOConverter::convert)
					.orElseThrow(() -> new ResourceNotFoundException("Can not update address because User " + id + " not found"));
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
		User user = userRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Can not delete user because user not found"));
		userRepository.delete(user);
	}
	
	public void updatingUsersMovieRating(String login, Rating rating) {
		userRepository
				.findByLogin(login)
				.map(user -> {
					user.getFilmsRatings().add(rating);
					rating.setUser(user);
					return user;
				})
				.orElseThrow(() -> new ResourceNotFoundException("User " + login + " not found"));
	}
}
