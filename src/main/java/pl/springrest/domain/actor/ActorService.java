package pl.springrest.domain.actor;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.springrest.converters.ActorDTOConverter;
import pl.springrest.dto.ActorDTO;

@Service
public class ActorService {

	private ActorRepository actorRepository;
	private ActorDTOConverter actorConverter;

	public ActorService(ActorRepository actorRepository, ActorDTOConverter actorConverter) {
		this.actorRepository = actorRepository;
		this.actorConverter = actorConverter;
	}

	/**
	 * Find actors in database and convert to DTO
	 * 
	 * @param lastName
	 *            actor's last name
	 * @return List of ActorDTO or null if actor not found
	 */
	public List<ActorDTO> findActorsBy(String lastName) {
		List<Actor> actors = actorRepository.findBylastName(lastName);
		if (actors == null)
			return null;
		else
			return actorConverter.convertAll(actors);
	}

	/**
	 * Save actor in database if actor not exist
	 * 
	 * @param actor
	 *            Actor to save
	 * @return ActorDTO or null if actor already exist in database
	 */
	public ActorDTO saveActor(Actor actor) {
		List<Actor> foundActor = actorRepository.findBylastName(actor.getLastName());
		if (foundActor != null && foundActor.contains(actor))
			return null;
		return actorConverter.convert(actorRepository.save(actor));
	}

}
