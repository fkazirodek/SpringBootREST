package pl.springrest.domain.actor;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.ActorListDTO;

@RestController
@RequestMapping(ActorController.BASE_URL)
class ActorController {

	public static final String BASE_URL = "/actors";
	
	private ActorService actorService;

	public ActorController(ActorService actorService) {
		this.actorService = actorService;
	}

	@GetMapping(value = "/{name}")
	public ActorListDTO getActorsByName(@PathVariable String name) {
		return actorService.getActorsBy(name);
	}
	
	@PostMapping
	public ResponseEntity<Void> addActor(@Valid @RequestBody ActorDTO actorDto, BindingResult bindingResult, UriComponentsBuilder uriBuilder) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		ActorDTO actorDTO = actorService.saveActor(actorDto);
		URI location = uriBuilder
						.path(BASE_URL)
						.path(actorDTO.getLastName())
						.build()
						.toUri();
		return ResponseEntity.created(location).build();
	}
}
