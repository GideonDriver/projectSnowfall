package com.training.bms.exceptions;

public class InvalidUsernameSizeException extends RuntimeException {
	public InvalidUsernameSizeException() {
	}

	public InvalidUsernameSizeException(String message) {
		super(message);
	}
}
