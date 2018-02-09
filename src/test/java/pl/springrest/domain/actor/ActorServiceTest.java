package pl.springrest.domain.actor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import pl.springrest.dto.ActorDTO;
import pl.springrest.utils.dto_converters.ActorDTOConverter;

public class ActorServiceTest {

	private static final String FIRST_NAME = "Jan";
	private static final String LAST_NAME = "Nowak";
	
	private ActorService actorService;
	private ActorDTOConverter actorConverter;
	
	@Mock
	private ActorRepository actorRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		actorConverter = new ActorDTOConverter();
		actorService = new ActorService(actorRepository, actorConverter);
	}
	
	@Test
	public void getActorByLastName() {		
		Actor actor = new Actor(FIRST_NAME, LAST_NAME);
		
		when(actorRepository.findByLastName(LAST_NAME))
			.thenReturn(Optional.of(Arrays.asList(actor)));
		
		ActorDTO actorDTO = actorService
								.getActorsBy(LAST_NAME)
								.getActors()
								.get(0);
		
		assertEquals(FIRST_NAME, actorDTO.getFirstName());
		assertEquals(LAST_NAME, actorDTO.getLastName());
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void getActorByLastNameThrowsResourceNotFoundExceptionEx() {		
		when(actorRepository.findByLastName(LAST_NAME))
			.thenThrow(ResourceNotFoundException.class);
		
		actorService.getActorsBy(LAST_NAME);
	}
	
	@Test
	public void saveActorReturnActorDTO() {
		Actor actor = new Actor(FIRST_NAME, LAST_NAME);
		ActorDTO actorDto = new ActorDTO(FIRST_NAME, LAST_NAME);
		
		when(actorRepository.save(any(Actor.class))).thenReturn(actor);
		
		ActorDTO savedActor = actorService.saveActor(actorDto);
		
		assertEquals(FIRST_NAME, savedActor.getFirstName());
		assertEquals(LAST_NAME, savedActor.getLastName());
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void saveActorThrowsDuplicateKeyEx() {
		
		when(actorRepository.save(any(Actor.class)))
			.thenThrow(new DuplicateKeyException(""));
		
		actorService.saveActor(new ActorDTO());
	}
}
