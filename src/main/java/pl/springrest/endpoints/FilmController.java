package pl.springrest.endpoints;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import pl.springrest.domain.actor.Actor;
import pl.springrest.domain.film.Film;
import pl.springrest.domain.film.FilmService;
import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.FilmDTO;

@RestController
@RequestMapping("/films")
public class FilmController {

	private FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FilmDTO> findAllFilms(@RequestParam int page, @RequestParam int size) {
		return filmService.getAllFilms(page, size);
	}

	@GetMapping(path = "/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FilmDTO> findFilmsByCategory(@PathVariable String category, 
											@RequestParam int page, 
											@RequestParam int size) {
		return filmService.getFilmsByCategory(category, page, size);
	}

	@GetMapping(path = "/film/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
	public FilmDTO findFilmByTitle(@PathVariable String title) {
		return filmService.getFilmByTitle(title);
	}
	
	@GetMapping(path="/film/{title}/actors", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ActorDTO> getActorsFromFilm(@PathVariable String title) {
		return filmService.getActorsFromFilmByTitle(title);
	}
	
	@PostMapping(path="/film", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addNewFilm(@Valid @RequestBody Film film, 
								BindingResult bindingResult, 
								UriComponentsBuilder uriBuilder) throws BindException {
		if(bindingResult.hasErrors()) 
			throw new BindException(bindingResult);
		FilmDTO filmDTO = filmService.saveFilm(film);
		URI location = uriBuilder
						.path("/films/film/")
						.path(filmDTO.getTitle())
							.build().toUri();
		return ResponseEntity.created(location).build();		
	}
	
	@PutMapping(path="/film/{title}/actors", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addActorsToFilm(@RequestBody List<Actor> actors, @PathVariable String title) {
		filmService.addActorsToFilm(actors, title);
	}
	
}
