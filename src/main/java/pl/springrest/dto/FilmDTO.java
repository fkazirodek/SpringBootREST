package pl.springrest.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.springrest.utils.LocalDateSerializer;

public class FilmDTO {

	private String title;
	private String description;
	private String category;

	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate dateRelease;

	public FilmDTO(String title, String description, String category, LocalDate dateRelease) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.dateRelease = dateRelease;
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

}
