package pl.springrest.endpoints;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.springrest.domain.user.Address;
import pl.springrest.domain.user.User;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.UserDTO;

@RunWith(SpringRunner.class)
public class UserControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private User user;
	private UserDTO userDto;

	@Before
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		user = new User("Jan", "Nowak", "login", "password", "user@email.com");
		userDto = new UserDTO(1L, "Jan", "Nowak", "login", "user@email.com");
	}

	@Test
	public void whenUserIsFoundThanHttpStatusIsOK() throws Exception {
		when(userService.getUserBy(1)).thenReturn(userDto);
		mockMvc.perform(get("/user/{1}", 1)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
				.andExpect(jsonPath("$.login", is(userDto.getLogin())));
		verify(userService, times(1)).getUserBy(1);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void whenUserIsNotFoundThanHttpStatusIsNOTFOUND() throws Exception {
		when(userService.getUserBy(2)).thenReturn(null);
		mockMvc.perform(get("/user/{id}", 2))
				.andExpect(status().isNotFound());
		verify(userService, times(1)).getUserBy(2);
		verifyNoMoreInteractions(userService);

	}

	@Test
	public void userSuccessfullyCreated() throws JsonProcessingException, Exception {
		when(userService.saveUser(user)).thenReturn(userDto);
		mockMvc.perform(post("/user/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isCreated())
				.andExpect(header().stringValues("location", "http://localhost/user/1"));
		verify(userService, times(1)).saveUser(user);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void duplicateUser() throws JsonProcessingException, Exception {
		when(userService.saveUser(user)).thenReturn(null);
		mockMvc.perform(post("/user/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isConflict());
		verify(userService, times(1)).saveUser(user);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void whenUserValidationIsFailThanHttpStatusBadRequest() throws JsonProcessingException, Exception {
		user.setLogin("");
		mockMvc.perform(post("/user/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenLoginFieldIsNotValidThanReturnLoginMessages() throws JsonProcessingException, Exception {
		String loginSizeMessage = "Login lenght must be between 3 and 15";
		String loginNotEmptyMessage = "Login can not be empty";
		user.setLogin("");
		MvcResult result = mockMvc.perform(post("/user/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(user)))
				.andReturn();
		assertThat(result.getResolvedException().getMessage()).contains(loginSizeMessage);
		assertThat(result.getResolvedException().getMessage()).contains(loginNotEmptyMessage);
	}
	
	@Test
	public void whenEmailFieldIsNotValidThanReturnEmailMessages() throws JsonProcessingException, Exception {
		String emailMessage = "You must enter the correct e-mail address";
		user.setEmail("a");
		MvcResult result = mockMvc.perform(post("/user/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(user)))
				.andReturn();
		assertThat(result.getResolvedException().getMessage()).contains(emailMessage);
	}
	
	@Test
	public void userSuccessfullyUpdated() throws JsonProcessingException, Exception {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		user.setAddress(address);
		userDto.setAddress(address);
		when(userService.updateAddress(address, 1)).thenReturn(userDto);
		mockMvc.perform(put("/user/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isCreated())
				.andExpect(header().stringValues("location", "http://localhost/user/1"));
		verify(userService, times(1)).updateAddress(address, 1);
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void userSuccessfullyDeleted() throws Exception {
		when(userService.deleteUser(1)).thenReturn(true);
		mockMvc.perform(delete("/user/{id}", 1)).andExpect(status().isOk());
		verify(userService, times(1)).deleteUser(1);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void noUserToDeleted() throws Exception {
		when(userService.deleteUser(1)).thenReturn(false);
		mockMvc.perform(delete("/user/{id}", 1)).andExpect(status().isNotFound());
		verify(userService, times(1)).deleteUser(1);
		verifyNoMoreInteractions(userService);
	}
}
