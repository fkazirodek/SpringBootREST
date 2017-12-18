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

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
