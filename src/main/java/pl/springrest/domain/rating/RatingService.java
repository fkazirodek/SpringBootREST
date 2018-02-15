package pl.springrest.domain.rating;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.springrest.domain.film.FilmService;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.FilmDTO;

@Service
@Transactional
public class RatingService {

	private RatingRepository ratingRepository;
	private UserService userService;
	private FilmService filmService;

	public RatingService(RatingRepository ratingRepository, UserService userService, FilmService filmService) {
		this.ratingRepository = ratingRepository;
		this.userService = userService;
		this.filmService = filmService;
	}

	/**
	 * This method allows add rating to film
	 * 
	 * @param userLogin
	 * @param filmTitle
	 * @param filmRating
	 */
	public FilmDTO addRatingToFilm(String userLogin, String filmTitle, double filmRating) {
		Rating rating = new Rating(filmRating);
		Rating savedRating = ratingRepository.save(rating);
		userService.updatingUsersMovieRating(userLogin, savedRating);
		return filmService.updateFilmRating(filmTitle, savedRating);
	}

}
