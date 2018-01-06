package pl.springrest.endpoints;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.MediaType;
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

import pl.springrest.domain.user.User;
import pl.springrest.domain.user.UserService;
import pl.springrest.dto.UserDTO;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDTO getUser(@PathVariable long id) {
		return userService.getUserBy(id);
	}

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> registerUser(@Valid @RequestBody User user, 
											BindingResult bindingResult,
											UriComponentsBuilder uriBuilder) throws BindException {
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);
		UserDTO userDto = userService.saveUser(user);
		URI locationUri = uriBuilder
							.path("/user/")
							.path(userDto.getId().toString())
								.build().toUri();
		return ResponseEntity.created(locationUri).build();
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateUser(@RequestBody User user, 
											@PathVariable long id,
											UriComponentsBuilder uriBuilder) {
		UserDTO userDto = userService.updateAddress(user.getAddress(), id);
		URI locationUri = uriBuilder
							.path("/user/")
							.path(userDto.getId().toString())
								.build().toUri();
		return ResponseEntity.created(locationUri).build();
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok().build();
	}

}
