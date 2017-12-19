package pl.springrest.dto;

public class UserDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String login;
	private String email;

	public UserDTO() {
	}

	public UserDTO(String firstName, String lastName, String login, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.email = email;
	}

	public UserDTO(Long id, String firstName, String lastName, String login, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
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

	public String getEmail() {
		return email;
	}

}
