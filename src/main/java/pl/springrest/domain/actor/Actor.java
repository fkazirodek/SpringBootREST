package pl.springrest.domain.actor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import pl.springrest.domain.BaseEntity;

@Entity
@Table(name = "actors")
@AttributeOverride(name = "id", column = @Column(name = "actor_id"))
public class Actor extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "firstname", nullable = false)
	private String firstName;

	@Column(name = "lastname", nullable = false)
	private String lastName;

	public Actor() {
	}

	public Actor(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
