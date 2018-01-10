package pl.springrest.domain.ratings;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.springrest.domain.film.Film;
import pl.springrest.domain.user.User;

@Entity
@Table(name="films_rating")
public class Rating implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="rating_id")
	private Long id;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "user_id") 
	private User user;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="film_id")
	private Film film;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
