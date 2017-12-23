package pl.springrest.endpoints;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.dto.FilmDTO;

@RunWith(SpringRunner.class)
public class FilmControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private FilmService filmService;
	
	@InjectMocks
	private FilmController filmController;
	
	private List<FilmDTO> films;
	private Film film;
	private FilmDTO filmDto1;
	private FilmDTO filmDto2;
	private final String category = "action";
	private final int page = 0;
	private final int size = 10;
	
	@Before
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
		film = new Film("Title1", "film description", "action", LocalDate.of(2015, 4, 28));
		filmDto1 = new FilmDTO("Title1", "film description", "action", LocalDate.of(2015, 4, 28));
		filmDto2 = new FilmDTO("Title2", "film description", "action", LocalDate.of(2016, 9, 28));
		films = new ArrayList<>();
		films.add(filmDto1);
		films.add(filmDto2);
	}
	
	@Test
	public void whenFilmsFoundThanStatusOK() throws Exception {
		when(filmService.getAllFilms(page, size)).thenReturn(films);
		mockMvc.perform(get("/films?page=0&size=10"))
							.andExpect(status().isOk());
		verify(filmService, times(1)).getAllFilms(page, size);
	}
	
	@Test
	public void whenFilmsNotFoundThanStatusNotFound() throws Exception {
		when(filmService.getAllFilms(page, size)).thenReturn(null);
		mockMvc.perform(get("/films?page=0&size=10"))
							.andExpect(status().isNotFound());
		verify(filmService, times(1)).getAllFilms(page, size);
	}
	
	@Test
	public void invokedMethodgetAllFilmsWithCorrectArguments() throws Exception {
		mockMvc.perform(get("/films?page=0&size=10"));
		verify(filmService, times(1)).getAllFilms(page, size);
	}
	
	@Test
	public void whenFilmsFoundByCategoryThanStatusOK() throws Exception {
		when(filmService.getFilmsByCategory(category, page, size)).thenReturn(films);
		mockMvc.perform(get("/films/action?page=0&size=10"))
							.andExpect(status().isOk());
	}
	
	@Test
	public void invokedMethodgetFilmsByCategoryWithCorrectArguments() throws Exception {
		when(filmService.getFilmsByCategory(category, page, size)).thenReturn(films);
		mockMvc.perform(get("/films/action?page=0&size=10"));
		verify(filmService, times(1)).getFilmsByCategory(category, page, size);
	}
	
	@Test
	public void whenFilmFoundByTitleThanStatusOK() throws Exception {
		String title = filmDto1.getTitle();
		when(filmService.getFilmByTitle(title)).thenReturn(filmDto1);
		mockMvc.perform(get("/films/film/{title}", title))
							.andExpect(status().isOk());
	}
	
	@Test
	public void invokedMethodgetFilmByTitleWithCorrectArguments() throws Exception {
		String title = filmDto1.getTitle();
		mockMvc.perform(get("/films/film/{title}", title));
		verify(filmService, times(1)).getFilmByTitle(title);
	}
	
//	@Test
//	public void filmSuccessfullyCreated() throws JsonProcessingException, Exception {
//		when(filmService.saveFilm(film)).thenReturn(filmDto1);
//		mockMvc.perform(post("/films/film/add")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(new ObjectMapper().writeValueAsString(film)))
//				.andExpect(status().isCreated())
//				.andExpect(header().stringValues("location", "http://localhost/films/film/Title1"));
//		verify(filmService, times(1)).saveFilm(film);
//		verifyNoMoreInteractions(filmService);
//	}
}