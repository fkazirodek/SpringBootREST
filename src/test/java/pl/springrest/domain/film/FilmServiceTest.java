package pl.springrest.domain.film;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import pl.springrest.domain.actor.Actor;
import pl.springrest.domain.rating.Rating;
import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.ActorListDTO;
import pl.springrest.dto.FilmDTO;
import pl.springrest.dto.FilmListDTO;
import pl.springrest.utils.dto_converters.ActorDTOConverter;
import pl.springrest.utils.dto_converters.FilmDTOConverter;

public class FilmServiceTest {

	private static final int SIZE = 10;
	private static final int PAGE = 0;
	
	private static final String FILM_ACT_TITLE = "Action Film";
	private static final String CATEGORY_ACTION = "action";
	private static final String CATEGORY_COMEDY = "comedy";
	private static final String FILM_DESCR = "film description";
	private static final LocalDate FILM_DATE = LocalDate.of(2015, 6, 24);
	
	private FilmService filmService;
	private FilmDTOConverter filmDTOConverter;
	private ActorDTOConverter actorDTOConverter;
	
	@Mock
	private FilmRepository filmRepository;
	
	private Film film_action;
	private Film film_comedy;
	private FilmDTO filmDto;
	
	private FilmListDTO filmListDTO;
	private ActorListDTO actorListDTO;
	private Pageable pageable;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		filmDTOConverter = new FilmDTOConverter();
		actorDTOConverter = new ActorDTOConverter();
		filmService = new FilmService(filmRepository, filmDTOConverter, actorDTOConverter);
		
		pageable = PageRequest.of(PAGE, SIZE);
		
		initData();
	}
	
	@Test
	public void getAllFilms() {
		when(filmRepository.findAll(pageable))
			.thenReturn((Page<Film>) new PageImpl<>(Arrays.asList(film_action, film_comedy), pageable, SIZE));
		
		FilmListDTO allFilms = filmService.getAllFilms(PAGE, SIZE);
		
		assertNotNull(allFilms.getFilms());
		assertThat(allFilms).hasSameClassAs(filmListDTO);
		assertEquals(2, allFilms.getFilms().size());
	}
	
	@Test
	public void getFilmsByCategoryReturnFilms() {
		when(filmRepository.findByCategoryOrderByRatingDesc(CATEGORY_ACTION, pageable))
			.thenReturn((Page<Film>) new PageImpl<>(Arrays.asList(film_action), pageable, SIZE));
		
		FilmListDTO filmsByCategory = filmService.getFilmsByCategory(CATEGORY_ACTION, PAGE, SIZE);
		FilmDTO foundFilmDTO = filmsByCategory.getFilms().get(0);
		
		assertNotNull(filmsByCategory.getFilms());
		assertThat(filmsByCategory).hasSameClassAs(filmListDTO);
		assertEquals(1, filmsByCategory.getFilms().size());
		compareFilm(foundFilmDTO, film_action);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void getFilmsByCategoryThrowsResourceNotFoundEx() {
		when(filmRepository.findByCategoryOrderByRatingDesc(CATEGORY_ACTION, pageable))
			.thenReturn((Page<Film>) new PageImpl<>(new ArrayList<Film>(), pageable, SIZE));
		
		filmService.getFilmsByCategory(CATEGORY_ACTION, PAGE, SIZE);
	}
	
	@Test
	public void getFilmByTitleReturnFilmDTO() {
		when(filmRepository.findByTitle(FILM_ACT_TITLE))
			.thenReturn(Optional.of(film_action));
		
		FilmDTO filmDTO = filmService.getFilmByTitle(film_action.getTitle());
		compareFilm(filmDTO, film_action);
		
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void ifFilmTitleNotFoundThrowsResourceNotFoundEx() {
		when(filmRepository.findByTitle(film_action.getTitle()))
			.thenReturn(Optional.empty());
		
		filmService.getFilmByTitle(film_action.getTitle());	
	}
	
	@Test
	public void saveFilmReturnSavedFilmDTO() {
		when(filmRepository.save(any(Film.class)))
			.thenReturn(film_action);
		
		FilmDTO filmDTO = filmService.saveFilm(filmDto);
		compareFilm(filmDTO, film_action);
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void ifFilmAlreadyExistThrowsDuplicateResourceEx() {
		when(filmRepository.save(any(Film.class)))
			.thenThrow(new DuplicateKeyException("Conflict"));
		
		filmService.saveFilm(filmDto);
		
	}
	
	@Test
	public void addActorsToFilmReturnFilmDTOwithActors() {
		ActorDTO actorDto = new ActorDTO("Tom", "Cruise");
		actorListDTO = new ActorListDTO(Arrays.asList(actorDto));
		
		when(filmRepository.findByTitle(FILM_ACT_TITLE))
			.thenReturn(Optional.of(film_action));
		
		FilmDTO returnedFilm = filmService.addActorsToFilm(actorListDTO, FILM_ACT_TITLE);
		Set<ActorDTO> actorsInFilm = returnedFilm.getActors();
		
		assertThat(actorsInFilm).isNotEmpty();
		assertThat(actorsInFilm).hasSize(1);
		assertTrue(actorsInFilm.contains(actorDto));
	}
	
	@Test
	public void getActorsFromFilmReturnActors() {
		ActorDTO actorDto = new ActorDTO("Tom", "Cruise");
		Set<Actor> actors = new HashSet<>(Arrays.asList(new Actor("Tom", "Cruise")));
		film_action.setActors(actors);
		
		when(filmRepository.findByTitle(FILM_ACT_TITLE))
			.thenReturn(Optional.of(film_action));
		
		List<ActorDTO> actorDTOs = filmService
									.getActorsFromFilmByTitle(FILM_ACT_TITLE)
									.getActors();
		
		assertThat(actorDTOs).isNotEmpty();
		assertThat(actorDTOs).hasSize(1);
		assertTrue(actorDTOs.contains(actorDto));
	}

	@Test
	public void updateFilmRating() {
		Rating r1 = new Rating(8.0);
		Rating r2 = new Rating(10);
		
		Set<Rating> filmRatings = film_action.getFilmRatings();
		filmRatings.add(r1);
		filmRatings.add(r2);
		double avgRating = (r1.getRating()+r2.getRating())/2;
		
		filmService.updateRating(film_action);
		assertEquals(avgRating, film_action.getRating(), 0.01);
		
	}
	
	private void initData() {
		film_action = new Film(FILM_ACT_TITLE, FILM_DESCR, CATEGORY_ACTION, FILM_DATE);
		film_comedy = new Film("Comedy", FILM_DESCR, CATEGORY_COMEDY, FILM_DATE);
		filmDto = new FilmDTO(FILM_ACT_TITLE, FILM_DESCR, CATEGORY_ACTION, FILM_DATE);
		
		filmListDTO = new FilmListDTO(Arrays.asList(filmDto));
	}
	
	private void compareFilm(FilmDTO filmDTO, Film film) {
		assertNotNull(filmDTO);
		assertThat(filmDTO.getTitle()).isEqualToIgnoringCase(film.getTitle());
		assertThat(filmDTO.getCategory()).isEqualToIgnoringCase(film.getCategory());
		assertThat(filmDTO).isNotSameAs(film);
	}
}
