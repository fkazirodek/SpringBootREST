package pl.springrest.domain.actor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ActorRepository extends JpaRepository<Actor, Long> {

	List<Actor> findBylastName(String lastName);
}
