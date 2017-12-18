package pl.springrest.endpoints;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.springrest.domain.user.User;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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
		user = new User(1, "Jan", "Nowak", "jann", "1234", "jann@gmail.com");
		userDto = new UserDTO(1L, "Jan", "Nowak", "jann", "jann@gmail.com");
	}

	@Test
	public void whenUserIsFoundThanHttpStatusIsOK() throws Exception {
		when(userService.getUserByID(1)).thenReturn(userDto);
		mockMvc.perform(get("/user/{1}", 1)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is("Jan")));
		verify(userService, times(1)).getUserByID(1);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void whenUserIsNotFoundThanHttpStatusIsNOTFOUND() throws Exception {
		when(userService.getUserByID(2)).thenReturn(null);
		mockMvc.perform(get("/user/{id}", 2)).andExpect(status().isNotFound());
		verify(userService, times(1)).getUserByID(2);
		verifyNoMoreInteractions(userService);

	}

	@Test
	public void userSuccessfullyCreated() throws JsonProcessingException, Exception {
		when(userService.saveUser(user)).thenReturn(userDto);
		mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isCreated())
				.andExpect(header().stringValues("location", "http://localhost/user/1"));
		verify(userService, times(1)).saveUser(user);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void duplicateUser() throws JsonProcessingException, Exception {
		when(userService.saveUser(user)).thenReturn(null);
		mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
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
	public void userSuccessfullyDeleted() throws Exception {
		when(userService.deleteUser(1)).thenReturn(true);
		mockMvc.perform(delete("/user/{id}/delete", 1)).andExpect(status().isOk());
		verify(userService, times(1)).deleteUser(1);
		verifyNoMoreInteractions(userService);
	}

	@Test
	public void noUserToDeleted() throws Exception {
		when(userService.deleteUser(1)).thenReturn(false);
		mockMvc.perform(delete("/user/{id}/delete", 1)).andExpect(status().isNotFound());
		verify(userService, times(1)).deleteUser(1);
		verifyNoMoreInteractions(userService);
	}
}
