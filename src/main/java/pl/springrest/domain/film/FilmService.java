package pl.springrest.domain.film;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.springrest.converters.FilmDTOConverter;
import pl.springrest.dto.FilmDTO;

@Service
@Transactional
public class FilmService {

	private FilmRepository filmRepository;
	private FilmDTOConverter filmDTOConverter;

	public FilmService(FilmRepository filmRepository, FilmDTOConverter filmDTOConverter) {
		this.filmRepository = filmRepository;
		this.filmDTOConverter = filmDTOConverter;
	}

	/**
	 * Get page with films
	 * 
	 * @param page
	 *            number of page
	 * @param size
	 *            page size
	 * @return List<FilmDTO> or null if films not found
	 */
	public List<FilmDTO> getAllFilms(int page, int size) {
		Page<Film> films = filmRepository.findAll(new PageRequest(page, size));
		return films.map(filmDTOConverter::convert).getContent();
	}

	/**
	 * Get page with films by category, sorted by date release
	 * 
	 * @param page
	 *            number of page
	 * @param size
	 *            page size
	 * @param category
	 *            film's category
	 * @return List<FilmDTO> or null if films not found
	 */
	public List<FilmDTO> getFilmsByCategory(String category, int page, int size) {
		Page<Film> filmsByCategory = filmRepository.findByCategoryOrderByRatingDesc(category,
				new PageRequest(page, size));
		return filmsByCategory.map(filmDTOConverter::convert).getContent();
	}

	/**
	 * Find and return film by title
	 * 
	 * @param title
	 *            film title
	 * @return FilmDTO or null when film not found
	 */
	public FilmDTO getFilmByTitle(String title) {
		Film film = filmRepository.findByTitle(title);
		if (film == null)
			return null;
		return filmDTOConverter.convert(film);
	}

}
