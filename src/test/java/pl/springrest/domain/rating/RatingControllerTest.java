package pl.springrest.domain.rating;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;

import org.apache.catalina.realm.GenericPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pl.springrest.dto.FilmDTO;
import pl.springrest.exceptions.GlobalControllerExceptionHandler;

public class RatingControllerTest {

	private static final String LOGIN = "user";
	private static final String PASSWORD = "password";
	
	private static final String FILM_TITLE = "title";
	private static final int RATING = 8;

	@Mock
	private RatingService ratingService;
	
	@InjectMocks
	private RatingController ratingController;
	
	private MockMvc mockMvc;
	private Principal principal;
	
	private FilmDTO filmDTO;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders
				.standaloneSetup(ratingController)
				.setControllerAdvice(new GlobalControllerExceptionHandler())
				.build();
		
		filmDTO = new FilmDTO(FILM_TITLE, "desc", "action", LocalDate.now());
		principal = new GenericPrincipal(LOGIN, PASSWORD, Collections.singletonList("ROLE_USER"));
	}
	
	@Test
	@WithMockUser(username="user", password="password")
	public void addRatingToFilm() throws Exception {
		filmDTO.setRating(RATING);
		when(ratingService.addRatingToFilm(eq(LOGIN), eq(FILM_TITLE), anyDouble())).thenReturn(filmDTO);
		
		mockMvc.perform(post(RatingController.BASE_URL + "/{title}", FILM_TITLE)
						.param("rating", Double.toString(RATING))
						.principal(principal))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.rating").exists())
					.andExpect(jsonPath("$.rating").value(RATING));
						
	}
}
