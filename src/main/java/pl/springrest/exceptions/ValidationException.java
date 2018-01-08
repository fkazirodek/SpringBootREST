package pl.springrest.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BindingResult bindingResult;
	private String message;
	
	public ValidationException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}

	public ValidationException(BindingResult bindingResult, String message) {
		this.bindingResult = bindingResult;
		this.message = message;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public String getMessage() {
		return message;
	}
	
	
	
	

}
