package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import pl.springrest.dto.UserDTO;
import pl.springrest.utils.dto_converters.UserDTOConverter;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	private UserService userService;
	private UserDTOConverter userDTOConverter;
	@Mock
	private UserRepository userRepository;

	private final long userID = 1;
	private User user;
	private UserDTO userDto;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void beforeMethod() {
		userDTOConverter = new UserDTOConverter();
		userService = new UserService(userRepository, userDTOConverter);
		user = new User("fn", "ln", "login", "pass", "user@email.com");
		userDto = new UserDTO("fn", "ln", "login", "pass", "user@email.com");
	}

	@Test
	public void getUserByIDShouldReturnUserDTO() {
		when(userRepository.findById(userID)).thenReturn(Optional.of(user));
		UserDTO userDTO = userService.getUserBy(userID);
		compareUser(userDTO);
	}

	@Test
	public void getUserByLoginShouldReturnUserDTO() {
		Optional<User> userOptional = Optional.of(user);
		String login = user.getLogin();
		when(userRepository.findByLogin(login)).thenReturn(userOptional);
		UserDTO userDTO = userService.getUserBy(login);
		compareUser(userDTO);
	}

	@Test
	public void whenGetUserByIdNotFoundUserShouldThrowsResourceNotFoundException() {
		when(userRepository.findById(userID)).thenReturn(Optional.empty());
		expectedException.expect(ResourceNotFoundException.class);
		userService.getUserBy(userID);
	}

	@Test
	public void saveUserWhenUserNotExistInDB() {
		when(userRepository.save(any(User.class))).thenReturn(user);
		UserDTO userDTO = userService.saveUser(userDto);
		compareUser(userDTO);
	}
	
	@Test
	public void whenSaveUserAndUserExistShouldThrowsDataIntegrityViolationException() {
		when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Conflict"));
		expectedException.expect(DataIntegrityViolationException.class);
		userService.saveUser(userDto);
	}
	
	@Test
	public void updateAddressWhenUserExist() {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		user.setAddress(address);
		when(userRepository.findById(userID)).thenReturn(Optional.of(user));
		UserDTO userDTO = userService.updateAddress(user.getAddress(), userID);
		compareUser(userDTO);
		
	}
	
	@Test
	public void noUpdateAddressWhenAddressNull() {
		when(userRepository.findById(userID)).thenReturn(Optional.of(user));
		UserDTO userDTO = userService.updateAddress(user.getAddress(), userID);
		assertNull(userDTO.getAddress());
		
	}
	
	private void compareUser(UserDTO userDTO) {
		assertNotNull(userDTO);
		assertThat(userDTO).isEqualToComparingFieldByField(user);
		assertThat(userDTO).isNotSameAs(user);
	}
}
