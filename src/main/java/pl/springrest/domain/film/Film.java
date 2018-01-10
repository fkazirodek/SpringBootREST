package pl.springrest.domain.film;

import java.io.Serializable;
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
import javax.persistence.OneToMany;

import pl.springrest.domain.actor.Actor;
import pl.springrest.domain.ratings.Rating;

@Entity
public class Film implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "film_id")
	private Long id;

	@Column(unique = true)
	private String title;

	@Column(nullable=false)
	private String description;
	
	@Column(nullable=false)
	private String category;

	@Column(name="date_release",nullable=false)
	private LocalDate dateRelease;

	@Column(nullable = true)
	private double rating;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "film_id"), 
				inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "actor_id"))
	private Set<Actor> actors = new HashSet<>();
	
	@OneToMany(mappedBy="film")
	Set<Rating> filmRatings = new HashSet<>();

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

	public LocalDate getDateRelease() {
		return dateRelease;
	}

	public void setDateRelease(LocalDate dateRelease) {
		this.dateRelease = dateRelease;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Set<Actor> getActors() {
		return actors;
	}

	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}

	public Set<Rating> getFilmRatings() {
		return filmRatings;
	}

	public void setFilmRatings(Set<Rating> filmRatings) {
		this.filmRatings = filmRatings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actors == null) ? 0 : actors.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((dateRelease == null) ? 0 : dateRelease.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
