package pl.springrest.domain.film;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmRepositoryTest {

	private static final String FILM_DESCRIPTION = "film description film description film description";
	private static final String CATEGORY_ACTION = "action";
	private static final String CATEGORY_HORROR = "horror";

	@Autowired
	private FilmRepository filmRepository;
	
	private PageRequest pageRequest; 
	private Film film1;
	private Film film2;
	private Film film3;
	
	@Before
	public void setUp() {
		pageRequest = PageRequest.of(0, 5);
		initData();
	}

	private void initData() {
		film1 = new Film("Title1", FILM_DESCRIPTION, CATEGORY_HORROR, LocalDate.of(2015, 4, 28));
		film2 = new Film("Title2", FILM_DESCRIPTION, CATEGORY_ACTION, LocalDate.of(2016, 9, 28));
		film3 = new Film("Title3", FILM_DESCRIPTION, CATEGORY_HORROR, LocalDate.of(2011, 9, 28));
		filmRepository.save(film1);
		filmRepository.save(film2);
		filmRepository.save(film3);
	}
	
	@After
	public void clean() {
		filmRepository.deleteAll();
	}

	@Test
	public void getAllFilms() {
		Page<Film> film = filmRepository.findAll(pageRequest);
		assertNotNull(film);
		assertThat(film.getContent()).hasSize(3);
	}
	
	@Test
	public void getFilmByCategoryReturnFilmsInOrder() {
		Page<Film> pageFilms = filmRepository.findByCategoryOrderByRatingDesc(CATEGORY_HORROR, pageRequest);
		assertNotNull(pageFilms);
		assertThat(pageFilms.getContent()).hasSize(2);
		assertEquals(Arrays.asList(film3, film1), pageFilms.getContent());
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void ifDescriptionNullThrowsDataIntegrityViolationException() {
		filmRepository.save(new Film("Title4", null, CATEGORY_HORROR, LocalDate.of(2015, 4, 28)));
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void ifDuplicateTitleThrowsDataIntegrityViolationException() {
		filmRepository.save(new Film("Title1", FILM_DESCRIPTION, CATEGORY_HORROR, LocalDate.of(2015, 4, 28)));
	}
	
	@Test
	public void getFilmByTitleReturnFilm() {
		String title = film1.getTitle();
		Film film = filmRepository
							.findByTitle(title)
							.get();
		assertNotNull(film);
		assertEquals(title, film.getTitle());
	}
	
	@Test
	public void ifFilmNotFoundReturnEmptyOptional() {
		Optional<Film> film = filmRepository.findByTitle("");
		assertFalse(film.isPresent());
	}
	
}
