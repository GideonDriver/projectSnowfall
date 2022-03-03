package com.training.bms.exceptions;

public class ExcessiveBalanceException extends RuntimeException {
	public ExcessiveBalanceException() {
	}

	public ExcessiveBalanceException(String message) {
		super(message);
	}
}
