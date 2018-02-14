package pl.springrest.domain.film;

import java.net.URI;

import javax.validation.Valid;

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

import pl.springrest.dto.ActorListDTO;
import pl.springrest.dto.FilmDTO;
import pl.springrest.dto.FilmListDTO;

@RestController
@RequestMapping(FilmController.BASE_URL)
class FilmController {

	public static final String BASE_URL = "/films";
	
	private FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping
	public FilmListDTO getAllFilms(@RequestParam int page, @RequestParam int size) {
		return filmService.getAllFilms(page, size);
	}

	@GetMapping("/category/{category}")
	public FilmListDTO getFilmsByCategory(@PathVariable String category, 
											@RequestParam int page, 
											@RequestParam int size) {
		return filmService.getFilmsByCategory(category, page, size);
	}

	@GetMapping("/{title}")
	public FilmDTO getFilmByTitle(@PathVariable String title) {
		return filmService.getFilmByTitle(title);
	}
	
	@GetMapping("/{title}/actors")
	public ActorListDTO getActorsFromFilm(@PathVariable String title) {
		return filmService.getActorsFromFilmByTitle(title);
	}
	
	@PostMapping
	public ResponseEntity<Void> addNewFilm(@Valid @RequestBody FilmDTO filmDto, 
											BindingResult bindingResult, 
											UriComponentsBuilder uriBuilder) throws BindException {
		if(bindingResult.hasErrors()) 
			throw new BindException(bindingResult);
		FilmDTO filmDTO = filmService.saveFilm(filmDto);
		URI location = uriBuilder
						.path(BASE_URL)
						.path(filmDTO.getTitle())
						.build()
						.toUri();
		return ResponseEntity.created(location).build();		
	}
	
	@PutMapping("/{title}/actors")
	public FilmDTO addActorsToFilm(@RequestBody ActorListDTO actors, @PathVariable String title) {
		return filmService.addActorsToFilm(actors, title);
	}
	
}
