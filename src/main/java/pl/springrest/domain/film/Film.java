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
import javax.validation.constraints.NotNull;
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
	@Size(min=20, max=250, message="{pl.springrest.domain.film.Film.description.Size}")
	private String description;
	
	@NotEmpty(message="{pl.springrest.domain.film.Film.category.NotEmpty}")
	private String category;
	
	@NotNull(message="{pl.springrest.domain.film.Film.yearRelease.NotNull}")
	@Column(name="year_release")
	private LocalDate dateRelease;
	
	@Column(nullable=true)
	private double rating;
	
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
	
	public double getRating() {
		return rating;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actors == null) ? 0 : actors.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((dateRelease == null) ? 0 : dateRelease.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		if (actors == null) {
			if (other.actors != null)
				return false;
		} else if (!actors.equals(other.actors))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (dateRelease == null) {
			if (other.dateRelease != null)
				return false;
		} else if (!dateRelease.equals(other.dateRelease))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
