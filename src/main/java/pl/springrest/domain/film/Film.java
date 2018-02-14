package pl.springrest.domain.film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import pl.springrest.domain.BaseEntity;
import pl.springrest.domain.actor.Actor;
import pl.springrest.domain.rating.Rating;

@Entity
@Table(name="films")
@AttributeOverride(name = "id", column = @Column(name = "film_id"))
@DynamicUpdate
public class Film extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
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
	@JoinTable(joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "id"), 
				inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
	private Set<Actor> actors = new HashSet<>();
	
	@OneToMany(mappedBy="film", cascade=CascadeType.MERGE)
	Set<Rating> filmRatings = new HashSet<>();

	public Film() {
	}
	
	public Film(String title, String description, String category, LocalDate yearRelease) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.dateRelease = yearRelease;
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

}
