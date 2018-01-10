package pl.springrest.domain.film;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import pl.springrest.domain.actor.ActorRepository;
import pl.springrest.dto.FilmDTO;
import pl.springrest.utils.dto_converters.ActorDTOConverter;
import pl.springrest.utils.dto_converters.FilmDTOConverter;

@RunWith(SpringRunner.class)
public class FilmServiceTest {

	private FilmService filmService;
	private FilmDTOConverter filmDTOConverter;
	private ActorDTOConverter actorDTOConverter;
	@Mock
	private FilmRepository filmRepository;
	@Mock
	private ActorRepository actorRepository;
	
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private Film film;
	private FilmDTO filmDto;
	
	@Before
	public void beforeMethod() {
		filmDTOConverter = new FilmDTOConverter();
		filmService = new FilmService(filmRepository, filmDTOConverter, actorRepository, actorDTOConverter);
		film = new Film("Title", "film description", "action", LocalDate.of(2015, 6, 24));
		filmDto = new FilmDTO("Title", "film description", "action", LocalDate.of(2015, 6, 24));
	}
	
	@Test
	public void whenFilmFoundByTitleShouldReturnFilmDTO() {
		when(filmRepository.findByTitle(film.getTitle())).thenReturn(Optional.of(film));
		FilmDTO filmDTO = filmService.getFilmByTitle(film.getTitle());
		compareFilm(filmDTO);
		
	}
	
	@Test
	public void whenFilmNotFoundByTitleShouldThrowsResourceNotFoundException() {
		when(filmRepository.findByTitle(film.getTitle())).thenReturn(Optional.empty());
		expectedException.expect(ResourceNotFoundException.class);
		filmService.getFilmByTitle(film.getTitle());	
	}
	
	@Test
	public void whenSaveFilmAndFilmNotExistShouldReturnFilmDTO() {
		when(filmRepository.save(film)).thenReturn(film);
		FilmDTO filmDTO = filmService.saveFilm(filmDto);
		compareFilm(filmDTO);
	}
	
	@Test
	public void whenSaveFilmAndFilmExistShouldThrowsDuplicateResourceException() {
		when(filmRepository.save(film)).thenThrow(new DuplicateKeyException("Conflict"));
		expectedException.expect(DuplicateKeyException.class);
		filmService.saveFilm(filmDto);
		
	}
	
	private void compareFilm(FilmDTO filmDTO) {
		assertNotNull(filmDTO);
		assertThat(filmDTO).isEqualToComparingFieldByField(film);
		assertThat(filmDTO).isNotSameAs(film);
	}
}
