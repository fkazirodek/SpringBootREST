package pl.springrest.converters;

import org.springframework.stereotype.Component;

import pl.springrest.domain.film.Film;
import pl.springrest.dto.FilmDTO;

@Component
public class FilmDTOConverter implements DTOConverter<Film, FilmDTO> {

	@Override
	public FilmDTO convert(Film film) {
		return new FilmDTO(film.getTitle(), film.getDescription(), film.getCategory(), film.getYearRelease());
	}
}
