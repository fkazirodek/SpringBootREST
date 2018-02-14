package pl.springrest.domain.rating;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.domain.user.User;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.FilmDTO;
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
	
	public RatingService(RatingRepository ratingRepository, 
						UserService userService, 
						FilmService filmService,
						UserDTOConverter userDTOConverter, 
						FilmDTOConverter filmDTOConverter) {
		
		this.ratingRepository = ratingRepository;
		this.userService = userService;
		this.filmService = filmService;
		this.userDTOConverter = userDTOConverter;
		this.filmDTOConverter = filmDTOConverter;
	}

	/**
	 * This method allows add rating to film
	 * 
	 * @param userLogin
	 * @param filmTitle
	 * @param filmRating
	 */
	public FilmDTO addRatingToFilm(String userLogin, String filmTitle, double filmRating) {
		//TODO Method don't work correctly, write better method and fix issues
		User userEntity = userDTOConverter.convert(userService.getUserBy(userLogin));
		Film filmEntity = filmDTOConverter.convert(filmService.getFilmByTitle(filmTitle));
		Rating rating = new Rating(filmRating);
		filmEntity.getFilmRatings().add(rating);
		FilmDTO filmDto = filmService.updateRating(filmEntity, rating);
		ratingRepository.save(rating);
		return filmDto;
	}

	

}
