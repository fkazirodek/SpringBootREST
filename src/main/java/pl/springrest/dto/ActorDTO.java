package pl.springrest.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class ActorDTO {

	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;

	public ActorDTO() {
	}

	public ActorDTO(String firstName, String lastName) {
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
