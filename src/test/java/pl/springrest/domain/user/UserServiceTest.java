package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import pl.springrest.dto.UserDTO;
import pl.springrest.dto_converter.UserDTOConverter;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	private UserService userService;
	private UserDTOConverter userDTOConverter;
	@Mock
	private UserRepository userRepository;

	private User user;

	@Before
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
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
		String login = user.getLogin();
		when(userRepository.findByLogin(login)).thenReturn(user);
		UserDTO userDTO = userService.getUserBy(login);
		compareUser(userDTO);
	}

	@Test
	public void getUserByShouldReturnNullIfUserNotExist() {
		when(userRepository.findOne(user.getId())).thenReturn(null);
		UserDTO userDTO = userService.getUserBy(user.getId());
		assertNull(userDTO);
	}

	@Test
	public void saveUserWhenUserNotExistInDB() {
		when(userRepository.findByLogin(user.getLogin())).thenReturn(null);
		when(userRepository.save(user)).thenReturn(user);
		UserDTO userDTO = userService.saveUser(user);
		compareUser(userDTO);
	}
	
	@Test
	public void saveUserShouldReturnNullIfUserExistInDB() {
		when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
		UserDTO userDTO = userService.saveUser(user);
		assertNull(userDTO);
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
