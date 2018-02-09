package pl.springrest.domain.rating;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.domain.user.User;
import pl.springrest.domain.user.UserService;
import pl.springrest.utils.dto_converters.FilmDTOConverter;
import pl.springrest.utils.dto_converters.UserDTOConverter;

@Service
@Transactional
public class RatingService {

	private RatingRepository ratingRepository;
	private UserService userService;
	private FilmService filmService;
	private UserDTOConverter userDTOConverter;
	private FilmDTOConverter filmDTOConverter;

	
	
	/**
	 * This method allows to add rating to film
	 * 
	 * @param userLogin
	 * @param filmTitle
	 * @param filmRating
	 */
	public void addRatingToFilm(String userLogin, String filmTitle, double filmRating) {
		User userEntity = userDTOConverter.convert(userService.getUserBy(userLogin));
		Film filmEntity = filmDTOConverter.convert(filmService.getFilmByTitle(filmTitle));
		Rating rating = new Rating(userEntity, filmEntity, filmRating);
		ratingRepository.save(rating);
		filmService.updateRating(filmEntity);
	}

	

}
