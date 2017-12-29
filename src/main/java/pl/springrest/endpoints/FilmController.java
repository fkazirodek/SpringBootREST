package pl.springrest.endpoints;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.dto.FilmDTO;

@RestController
@RequestMapping("/films")
public class FilmController {

	private FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FilmDTO> findAllFilms(@RequestParam int page, @RequestParam int size) throws ResourceNotFoundException {
		List<FilmDTO> films = filmService.getAllFilms(page, size);
		if (films == null)
			throw new ResourceNotFoundException("Films not found");
		return films;
	}

	@GetMapping(path = "/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FilmDTO> findFilmsByCategory(@PathVariable String category, 
										@RequestParam int page, 
										@RequestParam int size) throws ResourceNotFoundException {
		List<FilmDTO> filmsByCategory = filmService.getFilmsByCategory(category, page, size);
		if (filmsByCategory == null)
			throw new ResourceNotFoundException("Films not found");
		return filmsByCategory;
	}

	@GetMapping(path = "/film/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
	public FilmDTO findFilmByTitle(@PathVariable String title) throws ResourceNotFoundException {
		FilmDTO film = filmService.getFilmByTitle(title);
		if (film == null)
			throw new ResourceNotFoundException("Film " + title + " not found");
		return film;
	}
	
	@PostMapping(path="/film/add", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addNewFilm(@Valid @RequestBody Film film, 
								BindingResult bindingResult, 
								UriComponentsBuilder uriBuilder) throws BindException {
		if(bindingResult.hasErrors()) 
			throw new BindException(bindingResult);
		FilmDTO filmDTO = filmService.saveFilm(film);
		if(filmDTO == null)
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		URI location = uriBuilder.path("/films/film/").path(filmDTO.getTitle()).build().toUri();
		return ResponseEntity.created(location).build();		
	}
}
