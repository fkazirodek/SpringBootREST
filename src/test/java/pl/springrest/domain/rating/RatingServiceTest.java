package pl.springrest.domain.rating;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.FilmDTO;
import pl.springrest.dto.UserDTO;
import pl.springrest.utils.dto_converters.FilmDTOConverter;
import pl.springrest.utils.dto_converters.UserDTOConverter;

public class RatingServiceTest {

	private static final int RATING = 8;
	
	private static final String FILM_TITLE = "Action Film";
	private static final String CATEGORY_ACTION = "action";
	private static final String FILM_DESCR = "film description";
	private static final LocalDate FILM_DATE = LocalDate.of(2015, 6, 24);
	
	private static final String F_NAME = "Jan";
	private static final String L_NAME = "Nowak";
	private static final String LOGIN = "jannowak";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "user@email.com";
	
	private FilmDTO filmDTO;
	private UserDTO userDTO;
	private Rating rating;
	
	private RatingService ratingService;
	@Mock
	private RatingRepository ratingRepository;
	@Mock
	private FilmService filmService;
	@Mock
	private UserService userService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ratingService = new RatingService(ratingRepository, userService, filmService, new UserDTOConverter(), new FilmDTOConverter());
		
		filmDTO = new FilmDTO(FILM_TITLE, FILM_DESCR, CATEGORY_ACTION, FILM_DATE);
		userDTO = new UserDTO(F_NAME, L_NAME, LOGIN, PASSWORD, EMAIL);		
		rating = new Rating(RATING);
	}
	
	@Test
	public void addRatingToFilm() {
		when(filmService.getFilmByTitle(FILM_TITLE)).thenReturn(filmDTO);
		when(userService.getUserBy(LOGIN)).thenReturn(userDTO);
		when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
		
		when(filmService.updateRating(any(Film.class), any(Rating.class)))
		.then(a -> {
			filmDTO.setRating(rating.getRating());
			return filmDTO;
		});
		
		FilmDTO updatedFilmDTO = ratingService.addRatingToFilm(LOGIN, FILM_TITLE, RATING);
		
		assertEquals(RATING, updatedFilmDTO.getRating(), 0.001);
	}
	
}
