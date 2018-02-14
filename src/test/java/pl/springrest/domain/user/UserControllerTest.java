package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.springrest.dto.UserDTO;
import pl.springrest.dto.UserListDTO;
import pl.springrest.exceptions.GlobalControllerExceptionHandler;

public class UserControllerTest {

	private static final long ID = 1;
	private static final String F_NAME = "Jan";
	private static final String L_NAME = "Nowak";
	private static final String LOGIN = "jannowak";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "user@email.com";
	
	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private UserDTO userDto;

	@Before
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders
					.standaloneSetup(userController)
					.setControllerAdvice(new GlobalControllerExceptionHandler())
					.build();
		
		userDto = new UserDTO(ID, F_NAME, L_NAME, LOGIN, PASSWORD, EMAIL);
	}
	
	@Test
	public void getAllUsers() throws Exception {
		UserListDTO userListDTO = new UserListDTO(Arrays.asList(new UserDTO(), new UserDTO()));
		
		when(userService.getAllUsers()).thenReturn(userListDTO);
		
		mockMvc.perform(get(UserController.BASE_URL))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.users").isArray())
					.andExpect(jsonPath("$.users", hasSize(2)));
	}
	
	@Test
	public void getAllUsersReturnStatusNotFound() throws Exception {
		when(userService.getAllUsers()).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(get(UserController.BASE_URL))
					.andExpect(status().isNotFound());
	}
	
	@Test
	public void getUserByLoginReturnUserAndStatusOK() throws Exception {
		when(userService.getUserBy(LOGIN)).thenReturn(userDto);
		
		mockMvc.perform(get(UserController.BASE_URL + "/{login}", LOGIN))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.firstName").value(F_NAME))
				.andExpect(jsonPath("$.lastName").value(L_NAME))
				.andExpect(jsonPath("$.login").value(LOGIN))
				.andExpect(jsonPath("$.email").value(EMAIL));
		
		verify(userService, times(1)).getUserBy(LOGIN);
	}

	@Test
	public void getUserByLoginReturnStatusNotFound() throws Exception {
		when(userService.getUserBy(LOGIN)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(get(UserController.BASE_URL + "/{login}", LOGIN))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void getUserByIdReturnUserAndStatusOK() throws Exception {
		when(userService.getUserBy(ID)).thenReturn(userDto);
		
		mockMvc.perform(get(UserController.BASE_URL + "/user/{id}", ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.firstName").value(F_NAME))
				.andExpect(jsonPath("$.lastName").value(L_NAME))
				.andExpect(jsonPath("$.login").value(LOGIN))
				.andExpect(jsonPath("$.email").value(EMAIL));
		
		verify(userService, times(1)).getUserBy(ID);
	}
	
	@Test
	public void getUserByIdReturnStatusNotFound() throws Exception {
		when(userService.getUserBy(ID)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(get(UserController.BASE_URL + "/user/{id}", ID))
					.andExpect(status().isNotFound());
	}

	@Test
	public void userSuccessfullyCreated() throws JsonProcessingException, Exception {
		String resourceLocation = UserController.BASE_URL + "/user/" + ID;
		
		when(userService.saveUser(any(UserDTO.class)))
			.thenReturn(userDto);
		
		mockMvc.perform(post(UserController.BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJson(userDto)))
					.andExpect(status().isCreated())
					.andExpect(header().string("location", containsString(resourceLocation)));
		
		verify(userService, times(1)).saveUser(any(UserDTO.class));
	}

	@Test
	public void ifUserAlreadyExistReturnStatusConflict() throws Exception {
		when(userService.saveUser(any(UserDTO.class)))
			.thenThrow(new DuplicateKeyException("Conflict"));
		
		mockMvc.perform(post(UserController.BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJson(userDto)))
					.andExpect(status().isConflict());	
	}

	@Test
	public void ifUserValidationFailReturnStatusBadRequest() throws Exception {
		UserDTO invalidUserDto = new UserDTO("", L_NAME, LOGIN, PASSWORD, EMAIL);
		
		mockMvc.perform(post(UserController.BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJson(invalidUserDto)))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void ifLoginValidationFailReturnErrorMessages() throws Exception {
		String loginSizeMessage = "Login lenght must be between 3 and 15";
		String loginNotEmptyMessage = "Login can not be empty";
		
		UserDTO invalidUserDto = new UserDTO(F_NAME, L_NAME, "", PASSWORD, EMAIL);
		
		String contentAsString = mockMvc.perform(post(UserController.BASE_URL)
									.contentType(MediaType.APPLICATION_JSON_VALUE)
									.content(objectToJson(invalidUserDto)))
									.andReturn()
									.getResponse()
									.getContentAsString();
		
		assertThat(contentAsString).containsIgnoringCase(loginSizeMessage);
		assertThat(contentAsString).containsIgnoringCase(loginNotEmptyMessage);
	}
	
	@Test
	public void ifEmailValidationFailReturnErrorMessages() throws JsonProcessingException, Exception {
		String emailErrMessage = "You must enter the correct e-mail address";
		
		UserDTO invalidUserDto = new UserDTO(F_NAME, L_NAME, LOGIN, PASSWORD, "abc");
		
		String contentAsString = mockMvc.perform(post(UserController.BASE_URL)
									.contentType(MediaType.APPLICATION_JSON_VALUE)
									.content(objectToJson(invalidUserDto)))
									.andReturn()
									.getResponse()
									.getContentAsString();

		assertThat(contentAsString).containsIgnoringCase(emailErrMessage);
	}
	
	@Test
	public void userSuccessfullyUpdated() throws JsonProcessingException, Exception {
		String resourceLocation = UserController.BASE_URL + "/user/" + ID;
		
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		userDto.setAddress(address);
		
		when(userService.updateAddress(address, ID)).thenReturn(userDto);
		
		mockMvc.perform(put(UserController.BASE_URL + "/{id}", ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJson(userDto)))
					.andExpect(status().isOk())
					.andExpect(header().string("location", containsString(resourceLocation)));
	}
	
	@Test
	public void userSuccessfullyDeleted() throws Exception {
		doNothing().when(userService).deleteUser(ID);
		
		mockMvc.perform(delete(UserController.BASE_URL + "/{id}", ID))
					.andExpect(status().isOk());
	}

	@Test
	public void ifUserToDeleteNotFoundReturnStatusNotFound() throws Exception {
		doThrow(new ResourceNotFoundException()).when(userService).deleteUser(1);
		
		mockMvc.perform(delete(UserController.BASE_URL + "/{id}", 1))
					.andExpect(status().isNotFound());

	}
	
	private String objectToJson(Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}
}
