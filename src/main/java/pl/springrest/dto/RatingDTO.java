package pl.springrest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RatingDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(min=0, max=10, message="{pl.springrest.dto.RatingDTO.rating.Size}")
	private double rating;
	
	public RatingDTO() {
	}

	public RatingDTO(double rating) {
		this.rating = rating;
	}

	public double getRating() {
		return rating;
	}

}
