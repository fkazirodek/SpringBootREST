package pl.springrest.utils.dto_converters;

import org.springframework.stereotype.Component;

import pl.springrest.domain.actor.Actor;
import pl.springrest.dto.ActorDTO;

@Component
public class ActorDTOConverter implements DTOConverter<Actor, ActorDTO> {

	@Override
	public ActorDTO convert(Actor actor) {
		return new ActorDTO(actor.getFirstName(), actor.getLastName());
	}
	
	public Actor convert(ActorDTO actorDTO) {
		return new Actor(actorDTO.getFirstName(), actorDTO.getLastName());
	}
}
