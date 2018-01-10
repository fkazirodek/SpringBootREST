package pl.springrest.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.springrest.utils.LocalDateDeserializer;
import pl.springrest.utils.LocalDateSerializer;

public class FilmDTO {

	@NotEmpty(message = "{pl.springrest.dto.FilmDTO.title.NotEmpty}")
	private String title;
	
	@NotEmpty(message = "{pl.springrest.dto.FilmDTO.description.NotEmpty}")
	@Size(min = 20, max = 250, message = "{pl.springrest.dto.FilmDTO.description.Size}")
	private String description;
	
	@NotEmpty(message = "{pl.springrest.dto.FilmDTO.category.NotEmpty}")
	private String category;

	@NotNull(message = "{pl.springrest.dto.FilmDTO.yearRelease.NotNull}")
	@Column(name = "year_release")
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate dateRelease;
	
	private double rating;
	
	public FilmDTO() {
	}
	
	public FilmDTO(String title, String description, String category, LocalDate dateRelease) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.dateRelease = dateRelease;
	}
	
	public FilmDTO(String title, String description, String category, LocalDate dateRelease, double avgRating) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.dateRelease = dateRelease;
		this.rating = avgRating;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public LocalDate getDateRelease() {
		return dateRelease;
	}

	public double getRating() {
		return rating;
	}
}
