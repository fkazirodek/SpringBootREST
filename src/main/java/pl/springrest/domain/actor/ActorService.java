package pl.springrest.domain.actor;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import pl.springrest.dto.ActorDTO;
import pl.springrest.utils.dto_converters.ActorDTOConverter;

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
	 * @return List of ActorDTO
	 * @throws ResourceNotFoundException
	 *             if actor not found
	 */
	public List<ActorDTO> findActorsBy(String lastName) throws ResourceNotFoundException {
		List<Actor> actors = actorRepository
								.findBylastName(lastName)
									.orElseThrow(ResourceNotFoundException::new);
		return actorConverter.convertAll(actors);
	}

	/**
	 * Save actor in database if actor not exist
	 * 
	 * @param actor
	 *            Actor to save
	 * @return ActorDTO
	 * @throws DataIntegrityViolationException
	 *             if actor already exist in database
	 */
	public ActorDTO saveActor(ActorDTO actorDto) throws DuplicateKeyException {
		Actor savedActor;
		try {
			savedActor = actorRepository.save(actorConverter.convert(actorDto));
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateKeyException("Actor " + actorDto.getLastName() + " exist");
		}
		return actorConverter.convert(savedActor);
	}

}
