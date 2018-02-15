package pl.springrest.domain.rating;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.springrest.domain.BaseEntity;
import pl.springrest.domain.film.Film;
import pl.springrest.domain.user.User;

@Entity
@Table(name="films_rating")
@AttributeOverride(name = "id", column = @Column(name = "rating_id"))
public class Rating extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "user_id") 
	private User user;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="film_id")
	private Film film;
	
	@Column(nullable=false, updatable=true, precision=3)
	private double rating;
	
	public Rating() {
	}
	
	public Rating(double rating) {
		this.rating = rating;
	}
	
	public Rating(User user, Film film, double rating) {
		this.user = user;
		this.film = film;
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
