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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import pl.springrest.domain.actor.ActorService;
import pl.springrest.dto.ActorDTO;

@RestController
@RequestMapping("/actors")
public class ActorController {

	private ActorService actorService;

	public ActorController(ActorService actorService) {
		this.actorService = actorService;
	}

	@GetMapping(value = "actor/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ActorDTO> getActorByName(@PathVariable String name) {
		return actorService.findActorsBy(name);
	}
	
	@PostMapping(value="actor", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addActor(@Valid @RequestBody ActorDTO actorDto, BindingResult bindingResult, UriComponentsBuilder uriBuilder) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		ActorDTO actorDTO = actorService.saveActor(actorDto);
		URI location = uriBuilder.path("/actors/actor/").path(actorDTO.getLastName()).build().toUri();
		return ResponseEntity.created(location).build();
	}
}
