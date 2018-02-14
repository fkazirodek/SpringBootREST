package pl.springrest.domain.user;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import pl.springrest.dto.UserDTO;
import pl.springrest.dto.UserListDTO;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

	public static final String BASE_URL = "/users";
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public UserListDTO getUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/{login}")
	public UserDTO getUserByLogin(@PathVariable String login) {
		return userService.getUserBy(login);
	}
	
	@GetMapping("/user/{id}")
	public UserDTO getUserById(@PathVariable long id) {
		return userService.getUserBy(id);
	}

	@PostMapping
	public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDTO user, 
											BindingResult bindingResult,
											UriComponentsBuilder uriBuilder) throws BindException {
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);
		
		UserDTO userDto = userService.saveUser(user);
		URI resourceLocation = uriBuilder
								.path(BASE_URL)
								.path("/user/")
								.path(userDto.getId().toString())
								.build()
								.toUri();
		
		return ResponseEntity.created(resourceLocation).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUser(@RequestBody UserDTO user, 
											@PathVariable long id,
											UriComponentsBuilder uriBuilder) {
		
		UserDTO userDto = userService.updateAddress(user.getAddress(), id);
		URI resourceLocation = uriBuilder
								.path(BASE_URL)
								.path("/user/")
								.path(userDto.getId().toString())
								.build()
								.toUri();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.location(resourceLocation)
				.build();
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable long id) {
		userService.deleteUser(id);
	}

}
