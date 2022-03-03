package com.training.bms.exceptions;

public class InvalidUserInputException extends RuntimeException {
	public InvalidUserInputException() {
	}

	public InvalidUserInputException(String message) {
		super(message);
	}
}
