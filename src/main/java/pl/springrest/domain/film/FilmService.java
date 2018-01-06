package pl.springrest.domain.film;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
	 * @return List<FilmDTO>
	 * @throws ResourceNotFoundException
	 *             if films not found
	 */
	public List<FilmDTO> getAllFilms(int page, int size) throws ResourceNotFoundException {
		Page<Film> films = filmRepository.findAll(new PageRequest(page, size));
		if (films.getContent() == null)
			throw new ResourceNotFoundException("Films not found");
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
	 * @return List<FilmDTO>
	 * @throws ResourceNotFoundException
	 *             if films not found
	 */
	public List<FilmDTO> getFilmsByCategory(String category, int page, int size) throws ResourceNotFoundException {
		Page<Film> filmsByCategory = filmRepository.findByCategoryOrderByRatingDesc(category, new PageRequest(page, size));
		if (filmsByCategory.getContent() == null)
			throw new ResourceNotFoundException("Films not found");
		return filmsByCategory.map(filmDTOConverter::convert).getContent();
	}

	/**
	 * Find and return film by title
	 * 
	 * @param title
	 *            film title
	 * @return FilmDTO
	 * @throws ResourceNotFoundException
	 *             when film not found
	 */
	public FilmDTO getFilmByTitle(String title) throws ResourceNotFoundException {
		Film film = filmRepository
						.findByTitle(title)
							.orElseThrow(ResourceNotFoundException::new);
		return filmDTOConverter.convert(film);
	}

	/**
	 * Save film in database if film not exist
	 * 
	 * @param film
	 *            Film to save
	 * @return FilmDTO
	 * @throws DataIntegrityViolationException
	 *             if film already exist in database
	 */
	public FilmDTO saveFilm(Film film) throws DataIntegrityViolationException {
		return filmDTOConverter.convert(filmRepository.save(film));
	}

}
