package pl.springrest.domain.film;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface FilmRepository extends JpaRepository<Film, Long> {

	Optional<Film> findByTitle(String title);
	Page<Film> findByCategoryOrderByRatingDesc(String category, Pageable pageable);
}
