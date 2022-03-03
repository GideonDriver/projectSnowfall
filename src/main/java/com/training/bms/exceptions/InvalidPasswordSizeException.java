package com.training.bms.exceptions;

public class InvalidPasswordSizeException extends RuntimeException {
	public InvalidPasswordSizeException() {
	}

	public InvalidPasswordSizeException(String message) {
		super(message);
	}
}
