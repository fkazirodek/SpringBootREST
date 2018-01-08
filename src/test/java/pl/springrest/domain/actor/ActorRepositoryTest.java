package pl.springrest.domain.actor;

import static org.junit.Assert.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ActorRepositoryTest {

	@Autowired
	private ActorRepository actorRepository;
	
	private Actor actor;
	
	@Before
	public void beforeMethod() {
		actor = new Actor("Jan", "Nowak");
	}
	
	@After
	public void afterMethod() {
		actorRepository.deleteAll();
	}
	
	@Test
	public void findUserByFirstNameAndLastName() {
		actorRepository.save(actor);
		Optional<Actor> foundActor = actorRepository.findByFirstNameAndLastName(actor.getFirstName(), actor.getLastName());
		assertEquals(actor, foundActor.get());
	}
	
}
