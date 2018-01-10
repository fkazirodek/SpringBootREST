package pl.springrest.domain.ratings;

import java.util.OptionalDouble;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.domain.user.User;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.RatingDTO;
import pl.springrest.utils.dto_converters.RatingDTOConverter;

@Service
@Transactional
public class RatingService {

	private RatingRepository usersFilmsRepository;
	private UserService userService;
	private FilmService filmService;
	private RatingDTOConverter ratingDTOConverter;

	public RatingService(RatingRepository usersFilmsRepository, UserService userService, FilmService filmService,
			RatingDTOConverter ratingDTOConverter) {
		this.usersFilmsRepository = usersFilmsRepository;
		this.userService = userService;
		this.filmService = filmService;
		this.ratingDTOConverter = ratingDTOConverter;
	}

	/**
	 * 
	 * @param id
	 *            user id who adds a rating
	 * @param title
	 *            of the film to which the rating will be added
	 * @param ratingDTO
	 *            rating which will be added to film
	 */
	public void addRatingToFilm(Long id, String title, RatingDTO ratingDTO) {
		User userEntity = userService.getUserEntity(id);
		Film filmEntity = filmService.getFilmEntity(title);
		Rating rating = ratingDTOConverter.convert(ratingDTO);
		rating.setUser(userEntity);
		rating.setFilm(filmEntity);
		usersFilmsRepository.save(rating);
		OptionalDouble average = filmEntity.getFilmRatings()
												.stream()
													.map(Rating::getRating)
													.mapToDouble(Double::doubleValue)
													.average();
		filmEntity.setRating(average.getAsDouble());
	}

}
