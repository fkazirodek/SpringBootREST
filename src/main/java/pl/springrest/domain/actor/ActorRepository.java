package pl.springrest.domain.actor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.lang.String;
import pl.springrest.domain.actor.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

	Optional<List<Actor>> findByLastName(String lastName);
	Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);
}
