package pl.springrest.endpoints;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
		UserDTO userDto = userService.getUserBy(id);
		HttpStatus httpStatus = userDto == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
		return new ResponseEntity<UserDTO>(userDto, httpStatus);
	}

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerUser(@Valid @RequestBody User user, BindingResult bindingResult,
			UriComponentsBuilder uriBuilder) throws MethodArgumentNotValidException {
		if (bindingResult.hasErrors())
			throw new MethodArgumentNotValidException(null, bindingResult);
		UserDTO userDto = userService.saveUser(user);
		if (userDto == null)
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		URI locationUri = uriBuilder.path("/user/").path(userDto.getId().toString()).build().toUri();
		return ResponseEntity.status(HttpStatus.CREATED).location(locationUri).build();
	}

	@PutMapping(path = "/{id}/update")
	public ResponseEntity<Void> updateUser(@RequestBody User user, @PathVariable long id,
			UriComponentsBuilder uriBuilder) {
		UserDTO userDto = userService.updateAddress(user.getAddress(), id);
		if (userDto == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		URI locationUri = uriBuilder.path("/user/").path(userDto.getId().toString()).build().toUri();
		return ResponseEntity.status(HttpStatus.CREATED).location(locationUri).build();
	}

	@DeleteMapping(path = "/{id}/delete")
	public ResponseEntity<Void> deleteUser(@PathVariable long id) {
		HttpStatus status = userService.deleteUser(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		Map<String, Set<String>> errorsMap = fieldErrors.stream().collect(
									Collectors.groupingBy(FieldError::getField,
									Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())));
		return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
	}
}
