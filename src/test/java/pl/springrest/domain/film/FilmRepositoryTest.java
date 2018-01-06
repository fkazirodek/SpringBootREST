package pl.springrest.domain.film;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmRepositoryTest {

	@Autowired
	private FilmRepository filmRepository;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private PageRequest pageRequest; 
	private Film film1;
	private Film film2;
	private Film film3;
	
	@Before
	public void beforeMethod() {
		String filmDescr = "film description film description film description";
		pageRequest = new PageRequest(0, 3);
		film1 = new Film("Title1", filmDescr, "horror", LocalDate.of(2015, 4, 28));
		film2 = new Film("Title2", filmDescr, "action", LocalDate.of(2016, 9, 28));
		film3 = new Film("Title3", filmDescr, "horror", LocalDate.of(2011, 9, 28));
		film1.setRating(8.0);
		film2.setRating(6.0);
		film3.setRating(8.8);
		filmRepository.save(film1);
		filmRepository.save(film2);
		filmRepository.save(film3);
	}
	
	@After
	public void afterMethod() {
		filmRepository.deleteAll();
	}

	@Test
	public void ifFilmsExistReturnListNotNullAndSize2() {
		Page<Film> film = filmRepository.findAll(pageRequest);
		assertNotNull(film);
		assertThat(film.getContent()).hasSize(3);
	}
	
	@Test
	public void findFilmByCategoryShouldReturnTwoFilmInOrder() {
		String category = "horror";
		Page<Film> filmsByCategory = filmRepository.findByCategoryOrderByRatingDesc(category, pageRequest);
		assertNotNull(filmsByCategory);
		assertThat(filmsByCategory.getContent()).hasSize(2);
		assertThat(filmsByCategory.getContent().get(0)).isEqualToComparingFieldByField(film3);
		assertThat(filmsByCategory.getContent().get(1)).isEqualToComparingFieldByField(film1);
	}
	
	@Test
	public void ifDescriptionEmptyExpectedConstraintViolationException() {
		expectedException.expect(ConstraintViolationException.class);
		filmRepository.save(new Film("Title4", "", "horror", LocalDate.of(2015, 4, 28)));
	}
	
	@Test
	public void ifFilmFoundReturnFilm() {
		String title = film1.getTitle();
		Optional<Film> film = filmRepository.findByTitle(title);
		assertNotNull(film.get());
		assertThat(film.get()).isEqualToComparingFieldByField(film1);
	}
	
	@Test
	public void ifFilmNotFoundReturnNull() {
		String title = "T";
		Optional<Film> film = filmRepository.findByTitle(title);
		assertNull(film.orElse(null));
	}
	
}
