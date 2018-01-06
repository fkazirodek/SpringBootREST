package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import pl.springrest.converters.UserDTOConverter;
import pl.springrest.dto.UserDTO;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	private UserService userService;
	private UserDTOConverter userDTOConverter;
	@Mock
	private UserRepository userRepository;

	private User user;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void beforeMethod() {
		userDTOConverter = new UserDTOConverter();
		userService = new UserService(userRepository, userDTOConverter);
		user = new User(1L, "fn", "ln", "login", "pass", "user@email.com");
	}

	@Test
	public void getUserByIDShouldReturnUserDTO() {
		Long id = user.getId();
		when(userRepository.findOne(id)).thenReturn(user);
		UserDTO userDTO = userService.getUserBy(id);
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
		when(userRepository.findOne(user.getId())).thenReturn(null);
		expectedException.expect(ResourceNotFoundException.class);
		userService.getUserBy(user.getId());
	}

	@Test
	public void saveUserWhenUserNotExistInDB() {
		when(userRepository.findByLogin(user.getLogin())).thenReturn(null);
		when(userRepository.save(user)).thenReturn(user);
		UserDTO userDTO = userService.saveUser(user);
		compareUser(userDTO);
	}
	
	@Test
	public void whenSaveUserAndUserExistShouldThrowsDataIntegrityViolationException() {
		when(userRepository.save(user)).thenThrow(new DataIntegrityViolationException("Conflict"));
		expectedException.expect(DataIntegrityViolationException.class);
		userService.saveUser(user);
	}
	
	@Test
	public void updateAddressWhenUserExist() {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		user.setAddress(address);
		when(userRepository.findOne(user.getId())).thenReturn(user);
		UserDTO userDTO = userService.updateAddress(user.getAddress(), user.getId());
		compareUser(userDTO);
		
	}
	
	@Test
	public void noUpdateAddressWhenAddressNull() {
		when(userRepository.findOne(user.getId())).thenReturn(user);
		UserDTO userDTO = userService.updateAddress(user.getAddress(), user.getId());
		assertNull(userDTO.getAddress());
		
	}
	
	private void compareUser(UserDTO userDTO) {
		assertNotNull(userDTO);
		assertThat(userDTO).isEqualToComparingFieldByField(user);
		assertThat(userDTO).isNotSameAs(user);
	}
}
