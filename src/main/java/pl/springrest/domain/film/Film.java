package pl.springrest.domain.film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Film {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="film_id")
	private Long id;
	
	@NotEmpty(message="{pl.springrest.domain.film.Film.title.NotEmpty}")
	@Column(unique=true)
	private String title;
	
	@NotEmpty(message="{pl.springrest.domain.film.Film.description.NotEmpty}")
	@Size(min=50, max=250, message="{pl.springrest.domain.film.Film.description.Size}")
	private String description;
	
	@NotEmpty(message="{pl.springrest.domain.film.Film.category.NotEmpty}")
	private String category;
	
	@NotEmpty(message="{pl.springrest.domain.film.Film.yearRelease.NotEmpty}")
	@Column(name="year_release")
	private LocalDate dateRelease;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(joinColumns=@JoinColumn(name="film_id", referencedColumnName="film_id"),
				inverseJoinColumns=@JoinColumn(name="actor_id", referencedColumnName="actor_id"))	
	private Set<Actor> actors = new HashSet<>();

	public Film() {
	}

	public Film(String title, String description, String category, LocalDate yearRelease) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.dateRelease = yearRelease;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getYearRelease() {
		return dateRelease;
	}

	public void setYearRelease(LocalDate yearRelease) {
		this.dateRelease = yearRelease;
	}

	public Set<Actor> getActors() {
		return actors;
	}

	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}


}
