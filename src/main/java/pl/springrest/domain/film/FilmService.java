package pl.springrest.domain.film;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import pl.springrest.domain.actor.Actor;
import pl.springrest.domain.actor.ActorRepository;
import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.FilmDTO;
import pl.springrest.utils.dto_converters.ActorDTOConverter;
import pl.springrest.utils.dto_converters.FilmDTOConverter;

@Service
@Transactional
public class FilmService {

	private FilmRepository filmRepository;
	private ActorRepository actorRepository;
	private FilmDTOConverter filmDTOConverter;
	private ActorDTOConverter actorDTOConverter;

	public FilmService(FilmRepository filmRepository, FilmDTOConverter filmDTOConverter, ActorRepository actorRepository, ActorDTOConverter actorDTOConverter) {
		this.filmRepository = filmRepository;
		this.filmDTOConverter = filmDTOConverter;
		this.actorRepository = actorRepository;
		this.actorDTOConverter = actorDTOConverter;
	}
	
	/**
	 * Return User entity, use only in service layer
	 * @param title film title
	 * @return Film entity
	 */
	public Film getFilmEntity(String title) throws ResourceNotFoundException{
		return filmRepository.findByTitle(title).orElseThrow(ResourceNotFoundException::new);	
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
		if (films.getContent().isEmpty())
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
		Page<Film> filmsByCategory = filmRepository
										.findByCategoryOrderByRatingDesc(category,
																		new PageRequest(page, size));
		if (filmsByCategory.getContent().isEmpty())
			throw new ResourceNotFoundException("Films in category " + category + " not found");
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
	 * Find all actors who plays in film
	 * 
	 * @param title
	 *            film title to find
	 * @return List<ActorDTO> list of actors who plays in that film
	 * @throws ResourceNotFoundException
	 *             when film not found
	 */
	public List<ActorDTO> getActorsFromFilmByTitle(String title) {
		Film film = filmRepository
						.findByTitle(title)
							.orElseThrow(ResourceNotFoundException::new);
		return actorDTOConverter.convertAll(film.getActors());
	}

	/**
	 * Add all actors who plays in film
	 * 
	 * @param actors 
	 * 				who will be added to film
	 * @param title
	 *             of the film to which actors will be added
	 * @throws ResourceNotFoundException
	 *             when film not found
	 */
	public void addActorsToFilm(List<ActorDTO> actors, String title) {
		Film film = filmRepository
						.findByTitle(title)
							.orElseThrow(ResourceNotFoundException::new);
		for (ActorDTO actor : actors) {
			Optional<Actor> actorOptional = actorRepository
												.findByFirstNameAndLastName(actor.getFirstName(),
																			actor.getLastName());
			if (actorOptional.isPresent())
				film.getActors().add(actorOptional.get());
		}
	}

	/**
	 * Save film in database if film not exist
	 * 
	 * @param film
	 *            Film to save
	 * @return FilmDTO
	 * @throws DuplicateResourceException
	 *             if film already exist in database
	 */
	public FilmDTO saveFilm(FilmDTO filmDto) throws DuplicateKeyException {
		Film filmSaved;
		try {
			filmSaved = filmRepository.save(filmDTOConverter.convert(filmDto));
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateKeyException("Film '" + filmDto.getTitle() + "' already exist");
		}
		return filmDTOConverter.convert(filmSaved);
	}

}
