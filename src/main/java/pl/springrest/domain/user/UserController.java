package pl.springrest.domain.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

	public static final String BASE_URL = "/users";
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<UserDTO> getUsers() {
		return userService.getUsers();
	}
	
	@GetMapping("/{id}")
	public UserDTO getUser(@PathVariable long id) {
		return userService.getUserBy(id);
	}

	@PostMapping
	public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDTO user, 
											BindingResult bindingResult,
											UriComponentsBuilder uriBuilder) throws BindException {
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);
		UserDTO userDto = userService.saveUser(user);
		URI locationUri = uriBuilder
							.path(BASE_URL)
							.path(userDto.getId().toString())
							.build()
							.toUri();
		return ResponseEntity.created(locationUri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUser(@RequestBody UserDTO user, 
											@PathVariable long id,
											UriComponentsBuilder uriBuilder) {
		UserDTO userDto = userService.updateAddress(user.getAddress(), id);
		URI locationUri = uriBuilder
							.path(BASE_URL)
							.path(userDto.getId().toString())
							.build()
							.toUri();
		return ResponseEntity.created(locationUri).build();
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable long id) {
		userService.deleteUser(id);
	}

}
