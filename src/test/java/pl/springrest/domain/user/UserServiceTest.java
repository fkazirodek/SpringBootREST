package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import pl.springrest.dto.UserDTO;
import pl.springrest.utils.dto_converters.UserDTOConverter;

public class UserServiceTest {

	private static final long ID = 1;
	private static final String F_NAME = "Jan";
	private static final String L_NAME = "Nowak";
	private static final String LOGIN = "jannowak";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "user@email.com";
	
	private UserService userService;
	private UserDTOConverter userDTOConverter;
	
	@Mock
	private UserRepository userRepository;

	private User user;
	private UserDTO userDto;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		userDTOConverter = new UserDTOConverter();
		userService = new UserService(userRepository, userDTOConverter);
		
		user = new User(F_NAME, L_NAME, LOGIN, PASSWORD, EMAIL);
		userDto = new UserDTO(F_NAME, L_NAME, LOGIN, PASSWORD, EMAIL);
	}
	
	@Test
	public void getUsersShouldReturnUserList() {
		when(userRepository.findAll())
			.thenReturn(Arrays.asList(user));
		
		List<UserDTO> users = userService.getAllUsers().getUsers();
		
		assertNotNull(users);
		assertThat(users).hasSize(1);
		assertEquals(LOGIN, users.get(0).getLogin());
	}

	@Test
	public void getUserByIdReturnUserDTO() {
		when(userRepository.findById(ID))
			.thenReturn(Optional.of(user));
		
		UserDTO foundUser = userService.getUserBy(ID);
		assertUser(foundUser);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void getUserByIdThrowsResourceNotFoundEx() {
		when(userRepository.findById(ID))
			.thenReturn(Optional.empty());
		
		userService.getUserBy(ID);
	}

	@Test
	public void getUserByLoginReturnUserDTO() {
		when(userRepository.findByLogin(LOGIN))
			.thenReturn(Optional.of(user));
		
		UserDTO userDTO = userService.getUserBy(LOGIN);
		assertUser(userDTO);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void getUserByLoginThrowsResourceNotFoundEx() {
		when(userRepository.findByLogin(LOGIN))
			.thenReturn(Optional.empty());
		
		userService.getUserBy(LOGIN);
	}

	@Test
	public void saveUser() {
		when(userRepository.save(any(User.class)))
			.thenReturn(user);
		
		UserDTO userDTO = userService.saveUser(userDto);
		assertUser(userDTO);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void ifDuplicateUserThrows() {
		when(userRepository.save(any(User.class)))
			.thenThrow(new DataIntegrityViolationException(""));
		
		userService.saveUser(userDto);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void ifUserExistThrowsDataIntegrityViolationEx() {
		when(userRepository.save(any(User.class)))
			.thenThrow(new DataIntegrityViolationException("Conflict"));
		
		userService.saveUser(userDto);
	}
	
	@Test
	public void updateAddressIfUserExist() {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		user.setAddress(address);
		
		when(userRepository.findById(ID)).thenReturn(Optional.of(user));
		
		UserDTO userDTO = userService.updateAddress(address, ID);
		assertUser(userDTO);
		assertEquals(address, userDTO.getAddress());
	}
	
	@Test
	public void noUpdateAddressifAddressNull() {
		when(userRepository.findById(ID)).thenReturn(Optional.of(user));
		
		UserDTO userDTO = userService.updateAddress(user.getAddress(), ID);
		
		assertNull(userDTO.getAddress());
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void cannotUpdateAddressifUserNotFound() {
		when(userRepository.findById(ID)).thenReturn(Optional.empty());
		
		userService.updateAddress(user.getAddress(), ID);
	}
	
	@Test
	public void shouldDeleteUser() {
		when(userRepository.findById(ID))
			.thenReturn(Optional.of(user));
		doNothing().when(userRepository).delete(user);
		
		userService.deleteUser(ID);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void deleteUserShouldThrowResourceNotFoundExIfUserNotFound() {
		when(userRepository.findById(ID))
			.thenReturn(Optional.empty());
		
		userService.deleteUser(ID);
	}
	
	private void assertUser(UserDTO userDTO) {
		assertNotNull(userDTO);
		assertThat(userDTO.getLastName()).isEqualTo(L_NAME);
		assertThat(userDTO.getLogin()).isEqualTo(LOGIN);
		assertThat(userDTO.getEmail()).isEqualTo(EMAIL);
		assertThat(userDTO).isNotSameAs(user);
	}
}
