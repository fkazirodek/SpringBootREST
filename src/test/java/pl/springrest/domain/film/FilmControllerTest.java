package pl.springrest.domain.film;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.ActorListDTO;
import pl.springrest.dto.FilmDTO;
import pl.springrest.dto.FilmListDTO;
import pl.springrest.exceptions.GlobalControllerExceptionHandler;

public class FilmControllerTest {
	
	private static final int PAGE = 0;
	private static final int SIZE = 10;
	private static final String CATEGORY_ACTION = "action";
	private static final String FILM_TITLE = "Title1";
	
	@Mock
	private FilmService filmService;
	
	@InjectMocks
	private FilmController filmController;
	
	private MockMvc mockMvc;
	
	private FilmListDTO filmListDTO;
	private ActorListDTO actorListDTO;
	private FilmDTO filmDto1;
	private FilmDTO filmDto2;
		
	@Before
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders
					.standaloneSetup(filmController)
					.setControllerAdvice(new GlobalControllerExceptionHandler())
					.build();
		initData();
	}
	
	@Test
	public void getAllFilmsByPageReturnStatusOK() throws Exception {
		when(filmService.getAllFilms(PAGE, SIZE)).thenReturn(filmListDTO);
		
		mockMvc.perform(get(FilmController.BASE_URL)
						.param("page", Integer.toString(PAGE))
						.param("size", Integer.toString(SIZE)))
							.andExpect(status().isOk())
							.andExpect(jsonPath("$.films").isArray())
							.andExpect(jsonPath("$.films", hasSize(2)));
	}
	
	@Test
	public void ifFilmsNotFoundThanStatusNotFound() throws Exception {
		when(filmService.getAllFilms(PAGE, SIZE)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(get(FilmController.BASE_URL)
						.param("page", Integer.toString(PAGE))
						.param("size", Integer.toString(SIZE)))
							.andExpect(status().isNotFound());
	}
	
	@Test
	public void getFilmsByCategoryReturnStatusOK() throws Exception {
		when(filmService.getFilmsByCategory(CATEGORY_ACTION, PAGE, SIZE)).thenReturn(filmListDTO);
		
		mockMvc.perform(get(FilmController.BASE_URL + "/category/{category}", CATEGORY_ACTION)
						.param("page", Integer.toString(PAGE))
						.param("size", Integer.toString(SIZE)))
							.andExpect(status().isOk())
							.andExpect(jsonPath("$.films").isArray())
							.andExpect(jsonPath("$.films", hasSize(2)));
	}
	
	@Test
	public void getFilmByTitleReturnStatusOK() throws Exception {
		when(filmService.getFilmByTitle(FILM_TITLE)).thenReturn(filmDto1);
		
		mockMvc.perform(get(FilmController.BASE_URL + "/{title}", FILM_TITLE))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.title").value(FILM_TITLE));
	}
	
	@Test
	public void ifTitleNotFoundReturnStatusNotFound() throws Exception {
		String title = filmDto1.getTitle();
		when(filmService.getFilmByTitle(title)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(get(FilmController.BASE_URL + "/{title}", FILM_TITLE))
						.andExpect(status().isNotFound());
	}
	
	@Test
	public void getActorsFromFilm() throws Exception {
		actorListDTO = new ActorListDTO(Arrays.asList(new ActorDTO(), new ActorDTO()));
		
		when(filmService.getActorsFromFilmByTitle(anyString())).thenReturn(actorListDTO);
		
		mockMvc.perform(get(FilmController.BASE_URL + "/{title}/actors", FILM_TITLE))
							.andExpect(status().isOk())
							.andExpect(jsonPath("$.actors").isArray())
							.andExpect(jsonPath("$.actors", hasSize(2)));
	}
	
	@Test
	public void saveFilmReturnStatusCreated() throws Exception {
		when(filmService.saveFilm(any(FilmDTO.class))).thenReturn(filmDto1);
		
		mockMvc.perform(post(FilmController.BASE_URL)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectToJson(filmDto1)))
							.andExpect(status().isCreated());
	}
	
	@Test
	public void ifFilmNotValidReturnStausBadRequest() throws Exception {
		FilmDTO invalidFilmDTO = new FilmDTO("", "", CATEGORY_ACTION,  LocalDate.of(2015, 4, 28));
		
		when(filmService.saveFilm(any(FilmDTO.class))).thenReturn(invalidFilmDTO);
		
		mockMvc.perform(post(FilmController.BASE_URL)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectToJson(invalidFilmDTO)))
							.andExpect(status().isBadRequest());
	}
	
	@Test
	public void ifFilmNotValidReturnErrorMessages() throws Exception {
		final String titleErrorMessage = "Title name can not be empty";
		final String descErrorMessage = "Description can not be empty";
		final String descSizeErrMessage = "Description lenght must be between 20 and 250";
		
		FilmDTO invalidFilmDTO = new FilmDTO("", "", CATEGORY_ACTION,  LocalDate.of(2015, 4, 28));
		
		when(filmService.saveFilm(any(FilmDTO.class))).thenReturn(invalidFilmDTO);
		
		String contentAsString = mockMvc.perform(post(FilmController.BASE_URL)
									.contentType(MediaType.APPLICATION_JSON)
									.content(objectToJson(invalidFilmDTO)))
									.andReturn()
									.getResponse()
									.getContentAsString();
		
		assertThat(contentAsString).containsIgnoringCase(titleErrorMessage);
		assertThat(contentAsString).containsIgnoringCase(descErrorMessage);
		assertThat(contentAsString).containsIgnoringCase(descSizeErrMessage);
	}
	
	@Test
	public void addActorsToFilmReturnStatusOK() throws Exception {
		List<ActorDTO> actorDTOs = Arrays.asList(new ActorDTO("firstname", "lastname"));
		filmDto1.setActors(new HashSet<>(actorDTOs));
		actorListDTO = new ActorListDTO(actorDTOs);
		
		when(filmService.addActorsToFilm(any(ActorListDTO.class), eq(FILM_TITLE))).thenReturn(filmDto1);
		
		mockMvc.perform(put(FilmController.BASE_URL + "/{title}/actors", FILM_TITLE)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectToJson(actorListDTO)))
							.andExpect(status().isOk());
	}
	
	@Test
	public void addActorsToFilmReturnFilmDTOWithActors() throws Exception {
		List<ActorDTO> actorDTOs = Arrays.asList(new ActorDTO("Tom", "Cruise"));
		filmDto1.setActors(new HashSet<>(actorDTOs));
		actorListDTO = new ActorListDTO(actorDTOs);
		
		when(filmService.addActorsToFilm(any(ActorListDTO.class), eq(FILM_TITLE))).thenReturn(filmDto1);
		
		mockMvc.perform(put(FilmController.BASE_URL + "/{title}/actors", FILM_TITLE)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectToJson(actorListDTO)))
							.andExpect(jsonPath("$.actors").exists())
							.andExpect(jsonPath("$.actors").isArray())
							.andExpect(jsonPath("$.actors", hasSize(1)));
	}
	
	private String objectToJson(Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}
	
	private void initData() {
		final String description = "film description film description film description";
		filmDto1 = new FilmDTO(FILM_TITLE, description, CATEGORY_ACTION, LocalDate.of(2015, 4, 28));
		filmDto2 = new FilmDTO("Title2", description, CATEGORY_ACTION, LocalDate.of(2016, 9, 28));
		List<FilmDTO> films = new ArrayList<>();
		films.add(filmDto1);
		films.add(filmDto2);
		filmListDTO = new FilmListDTO(films);
	}
	
}
