package pl.springrest.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.validation.constraints.NotEmpty;

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
	
	private Set<ActorDTO> actors;
	
	private double rating;
	
	public FilmDTO() {
	}
	
	public FilmDTO(String title, String description, String category, LocalDate dateRelease) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.dateRelease = dateRelease;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getDateRelease() {
		return dateRelease;
	}

	public void setDateRelease(LocalDate dateRelease) {
		this.dateRelease = dateRelease;
	}

	public Set<ActorDTO> getActors() {
		return actors;
	}

	public void setActors(Set<ActorDTO> actors) {
		this.actors = actors;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateRelease == null) ? 0 : dateRelease.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilmDTO other = (FilmDTO) obj;
		if (dateRelease == null) {
			if (other.dateRelease != null)
				return false;
		} else if (!dateRelease.equals(other.dateRelease))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
