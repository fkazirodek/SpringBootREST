package pl.springrest.endpoints;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
		UserDTO userDto = userService.getUserBy(id);
		HttpStatus httpStatus = userDto == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
		return new ResponseEntity<UserDTO>(userDto, httpStatus);
	}

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult, UriComponentsBuilder uriBuilder) {
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		UserDTO userDto = userService.saveUser(user);
		if (userDto == null)
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		HttpHeaders httpHeaders = new HttpHeaders();
		URI locationUri = uriBuilder.path("/user/").path(userDto.getId().toString()).build().toUri();
		httpHeaders.setLocation(locationUri);
		return ResponseEntity.status(HttpStatus.CREATED).location(locationUri).build();
	}

	@DeleteMapping(path = "/{id}/delete")
	public ResponseEntity<Void> deleteUser(@PathVariable long id) {
		HttpStatus status = userService.deleteUser(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status).build();
	}
	

}
