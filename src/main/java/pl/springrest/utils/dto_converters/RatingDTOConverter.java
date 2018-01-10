package pl.springrest.utils.dto_converters;

import org.springframework.stereotype.Component;

import pl.springrest.domain.ratings.Rating;
import pl.springrest.dto.RatingDTO;

@Component
public class RatingDTOConverter implements DTOConverter<Rating, RatingDTO> {

	@Override
	public RatingDTO convert(Rating rating) {
		return new RatingDTO(rating.getRating());
	}

	public Rating convert(RatingDTO ratingDTO) {
		return new Rating(ratingDTO.getRating());
	}
	
}
