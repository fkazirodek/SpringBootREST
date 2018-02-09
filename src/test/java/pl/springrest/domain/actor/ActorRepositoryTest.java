package pl.springrest.domain.actor;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActorRepositoryTest {

	private static final String LAST_NAME = "Nowak";
	private static final String FIRST_NAME = "Jan";
	
	@Autowired
	private ActorRepository actorRepository;
	
	private Actor actor;
	
	@Before
	public void setUp() {
		actor = new Actor(FIRST_NAME, LAST_NAME);
	}
	
	@After
	public void afterMethod() {
		actorRepository.deleteAll();
	}
	
	@Test
	public void getActorsByLastNameReturnActors() {
		actorRepository.save(actor);
		List<Actor> actors = actorRepository
								.findByLastName(LAST_NAME)
								.orElse(Collections.emptyList());
		assertThat(actors, hasSize(1));
	}
	
	@Test
	public void gatActorsByLastNameReturnEmptyList() {
		List<Actor> actors = actorRepository
								.findByLastName(LAST_NAME)
								.orElse(Collections.emptyList());
		assertTrue(actors.isEmpty());
	}
	
	@Test
	public void getActorByFullNameReturnActor() {
		actorRepository.save(actor);
		Optional<Actor> foundActor = actorRepository
										.findByFirstNameAndLastName(actor.getFirstName(), actor.getLastName());
		assertEquals(actor, foundActor.get());
	}
	
}
