package pl.springrest.domain.actor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import pl.springrest.dto.ActorDTO;
import pl.springrest.dto.ActorListDTO;
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
	 * @return ActorListDTO
	 * @throws ResourceNotFoundException
	 *             if actor not found
	 */
	public ActorListDTO getActorsBy(String lastName) {
		return actorRepository
						.findByLastName(lastName)
							.map(actorConverter::convertAll)
							.map(ActorListDTO::new)
							.orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * Save actor in database if actor not exist
	 * 
	 * @param actor
	 *            Actor to save
	 * @return ActorDTO
	 * @throws DuplicateKeyException
	 *             if actor already exist in database
	 */
	public ActorDTO saveActor(ActorDTO actorDto) {
		Actor savedActor;
		try {
			savedActor = actorRepository.save(actorConverter.convert(actorDto));
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateKeyException("Actor " + actorDto.getLastName() + " exist");
		}
		return actorConverter.convert(savedActor);
	}

}
