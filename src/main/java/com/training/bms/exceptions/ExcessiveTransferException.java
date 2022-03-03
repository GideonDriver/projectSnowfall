package com.training.bms.exceptions;

public class ExcessiveTransferException extends RuntimeException {
	public ExcessiveTransferException() {
	}

	public ExcessiveTransferException(String message) {
		super(message);
	}
}
