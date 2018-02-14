package pl.springrest.domain.rating;

import java.security.Principal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.springrest.dto.FilmDTO;

@RestController
@RequestMapping(RatingController.BASE_URL)
public class RatingController {

	public static final String BASE_URL = "/ratings";

	private RatingService ratingService;
	
	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@PostMapping("/{title}")
	public FilmDTO rateFilm(@PathVariable String title, @RequestParam String rating, Principal principal) {
		return ratingService.addRatingToFilm(principal.getName(), title, Double.parseDouble(rating));
	}
}
