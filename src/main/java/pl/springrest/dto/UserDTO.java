package pl.springrest.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.springrest.domain.user.Address;

public class UserDTO {

	@JsonIgnore
	private Long id;
	
	@NotEmpty(message="{pl.springrest.dto.UserDTO.firstName.NotEmpty}")
	@Size(max=15, message="{pl.springrest.dto.UserDTO.firstName.Size}")
	private String firstName;
	
	private String lastName;
	
	@NotEmpty(message="{pl.springrest.dto.UserDTO.login.NotEmpty}")
	@Size(min=3, max=15, message="{pl.springrest.dto.UserDTO.login.Size}")
	private String login;
	
	@NotEmpty(message="{pl.springrest.dto.UserDTO.password.NotEmpty}")
	@Size(min=6,max=15, message="{pl.springrest.dto.UserDTO.password.Size{")
	private String password;
	
	@NotEmpty(message="{pl.springrest.dto.UserDTO.email.NotEmpty}")
	@Email(message="{pl.springrest.dto.UserDTO.email.Email}")
	private String email;

	private Address address;
	
	public UserDTO() {
	}

	public UserDTO(String firstName, String lastName, String login, String password, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.email = email;
	}

	public UserDTO(Long id, String firstName, String lastName, String login, String password, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}

}
