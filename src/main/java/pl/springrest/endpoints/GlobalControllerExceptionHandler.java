package pl.springrest.endpoints;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Resources not found")
	public String handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	protected Map<String, Set<String>> handleBindException(BindException ex) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		Map<String, Set<String>> errorsMap = fieldErrors.stream().collect(
						Collectors.groupingBy(FieldError::getField,
						Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())));
		return errorsMap;
	}
}
