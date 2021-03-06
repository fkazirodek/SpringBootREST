package pl.springrest.domain.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import pl.springrest.domain.BaseEntity;
import pl.springrest.domain.rating.Rating;

@Entity
@Table(name="users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@DynamicUpdate
public class User extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(name="firstname")
	private String firstName;
	
	@Column(name="lastname", nullable=true)
	private String lastName;
	
	
	@Column(unique=true, nullable=false)
	private String login;
	
	@Column(nullable=false)
	private String password;
	
	@Column(unique=true, nullable=false)
	private String email;
	
	@Embedded
	private Address address;
	
	@OneToMany(mappedBy="user")
	private Set<Rating> filmsRatings = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();
	

	public User() {}

	public User(String login, String password, String email) {
		this.login = login;
		this.password = password;
		this.email = email;
	}

	public User(String firstName, String lastName, String login, String password, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.email = email;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Rating> getFilmsRatings() {
		return filmsRatings;
	}

	public void setFilmsRatings(Set<Rating> filmsRatings) {
		this.filmsRatings = filmsRatings;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	
}
