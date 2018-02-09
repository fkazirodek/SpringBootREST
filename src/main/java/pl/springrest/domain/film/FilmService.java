package pl.springrest.domain.film;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import pl.springrest.domain.actor.Actor;
import pl.springrest.domain.rating.Rating;
import pl.springrest.dto.ActorListDTO;
import pl.springrest.dto.FilmDTO;
import pl.springrest.dto.FilmListDTO;
import pl.springrest.utils.dto_converters.ActorDTOConverter;
import pl.springrest.utils.dto_converters.FilmDTOConverter;

@Service
@Transactional
public class FilmService {

	private FilmRepository filmRepository;
	private FilmDTOConverter filmDTOConverter;
	private ActorDTOConverter actorDTOConverter;

	public FilmService(FilmRepository filmRepository, FilmDTOConverter filmDTOConverter, ActorDTOConverter actorDTOConverter) {
		this.filmRepository = filmRepository;
		this.filmDTOConverter = filmDTOConverter;
		this.actorDTOConverter = actorDTOConverter;
	}
	
	/**
	 * Get page with films
	 * 
	 * @param page
	 *            number of page
	 * @param size
	 *            page size
	 * @return FilmListDTO
	 * @throws ResourceNotFoundException
	 *             if films not found
	 */
	public FilmListDTO getAllFilms(int page, int size) {
		Page<Film> films = filmRepository.findAll(PageRequest.of(page, size));
		if (films.getContent().isEmpty())
			throw new ResourceNotFoundException("Films not found");

		 return new FilmListDTO(films
				 				.map(filmDTOConverter::convert)
				 				.getContent());
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
	 * @return FilmListDTO
	 * @throws ResourceNotFoundException
	 *             if films not found
	 */
	public FilmListDTO getFilmsByCategory(String category, int page, int size) {
		Page<Film> filmsByCategory = filmRepository
										.findByCategoryOrderByRatingDesc(category, PageRequest.of(page, size));
		if (filmsByCategory.getContent().isEmpty())
			throw new ResourceNotFoundException("Films in category " + category + " not found");
		
		return new FilmListDTO(filmsByCategory
								.map(filmDTOConverter::convert)
								.getContent());
	}

	/**
	 * Find film by title
	 * 
	 * @param title
	 *            film title
	 * @return FilmDTO
	 * @throws ResourceNotFoundException
	 *             when film not found
	 */
	public FilmDTO getFilmByTitle(String title) {
		return filmRepository
					.findByTitle(title)
						.map(filmDTOConverter::convert)
						.orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * Find all actors who plays in film
	 * 
	 * @param title
	 *            film title to find
	 * @return ActorListDTO list of actors who plays in that film
	 * @throws ResourceNotFoundException
	 *             when film not found
	 */
	public ActorListDTO getActorsFromFilmByTitle(String title) {
		return filmRepository
					.findByTitle(title)
						.map(Film::getActors)
						.map(actorDTOConverter::convertAll)
						.map(ActorListDTO::new)
						.orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * Add all actors who plays in film
	 * 
	 * @param actorsDto 
	 * 				who will be added to film
	 * @param title
	 *             of the film to which actors will be added
	 * @throws ResourceNotFoundException
	 *             when film not found
	 */
	public FilmDTO addActorsToFilm(ActorListDTO actorsDto, String title) {
		Set<Actor> actors = actorsDto
								.getActors()
								.stream()
									.map(actorDTOConverter::convert)
									.collect(Collectors.toSet());
		Film film = filmRepository
						.findByTitle(title)
						.orElseThrow(ResourceNotFoundException::new);
		film.getActors().addAll(actors);
		return filmDTOConverter.convert(film);
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
	public FilmDTO saveFilm(FilmDTO filmDto) {
		Film filmSaved;
		try {
			filmSaved = filmRepository.save(filmDTOConverter.convert(filmDto));
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateKeyException("Film '" + filmDto.getTitle() + "' already exist");
		}
		return filmDTOConverter.convert(filmSaved);
	}

	
	public void updateRating(Film film) {
		film.setRating(getAverageRating(film));
	}
	
	private double getAverageRating(Film filmEntity) {
		return filmEntity
				.getFilmRatings()
					.stream()
						.map(Rating::getRating)
						.mapToDouble(Double::doubleValue)
						.average()
						.getAsDouble();
	}
}
