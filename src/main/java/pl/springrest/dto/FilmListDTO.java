package pl.springrest.dto;

import java.util.List;

public class FilmListDTO {

	private List<FilmDTO> films;

	public FilmListDTO() {}

	public FilmListDTO(List<FilmDTO> films) {
		this.films = films;
	}

	public List<FilmDTO> getFilms() {
		return films;
	}

	public void setFilms(List<FilmDTO> films) {
		this.films = films;
	}

}
