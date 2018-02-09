package pl.springrest.domain.actor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.List;

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

import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.ActorListDTO;
import pl.springrest.exceptions.GlobalControllerExceptionHandler;

public class ActorControllerTest {

	private static final String FIRST_NAME = "Jan";
	private static final String LAST_NAME = "Nowak";

	@Mock
	private ActorService actorService;
	
	@InjectMocks
	private ActorController actorController;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
					.standaloneSetup(actorController)
					.setControllerAdvice(new GlobalControllerExceptionHandler())
					.build();
	}
	
	@Test
	public void getActorsByNameReturnStatusOK() throws Exception {
		ActorDTO actorDTO = new ActorDTO(FIRST_NAME, LAST_NAME);
		
		List<ActorDTO> actorsDTO = Arrays.asList(actorDTO);
		ActorListDTO actorListDTO = new ActorListDTO(actorsDTO);
		
		when(actorService.getActorsBy(LAST_NAME)).thenReturn(actorListDTO);
		
		mockMvc.perform(get(ActorController.BASE_URL + "/" + LAST_NAME)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.actors", hasSize(1)));
	}
	
	@Test
	public void getActorsByNameReturnStatusNotFound() throws Exception {
		when(actorService.getActorsBy(LAST_NAME)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(get(ActorController.BASE_URL + "/" + LAST_NAME)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void addActorReturnStatusCreated() throws Exception {
		ActorDTO actorDTO = new ActorDTO(FIRST_NAME, LAST_NAME);
		when(actorService.saveActor(any(ActorDTO.class))).thenReturn(actorDTO);
		
		mockMvc.perform(post(ActorController.BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectToJson(actorDTO)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void addActorReturnStatusBadRequestIfNameIsEmpty() throws Exception {
		ActorDTO actorDTO = new ActorDTO("", LAST_NAME);
		
		mockMvc.perform(post(ActorController.BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectToJson(actorDTO)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void ifActorNotValidReturnErrorMessages() throws Exception {
		final String firstNameMessage = "First name can not be empty";
		final String lastNameMessage = "Last name can not be empty";
		
		ActorDTO actorDTO = new ActorDTO("", "");
		
		String contentAsString = mockMvc.perform(post(ActorController.BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJson(actorDTO)))
						.andReturn()
						.getResponse()
						.getContentAsString();
		
		assertThat(contentAsString).containsIgnoringCase(firstNameMessage);
		assertThat(contentAsString).containsIgnoringCase(lastNameMessage);
	}
	
	@Test
	public void ifActorAlreadyExistThrowsDuplicateKeyException() throws Exception {
		ActorDTO actorDTO = new ActorDTO(FIRST_NAME, LAST_NAME);
		String errorMessage = "Actor " + LAST_NAME + " exist";
		
		when(actorService.saveActor(any(ActorDTO.class))).thenThrow(new DuplicateKeyException(errorMessage));
		
		String contentAsString = mockMvc.perform(post(ActorController.BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectToJson(actorDTO)))
				.andExpect(status().isConflict())
				.andReturn()
				.getResponse()
				.getContentAsString();
		
		assertThat(contentAsString).containsIgnoringCase(errorMessage);
	}
	
	private String objectToJson(Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}
}
