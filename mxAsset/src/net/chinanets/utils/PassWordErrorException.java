package net.chinanets.utils;

public class PassWordErrorException extends Exception {
	
	public PassWordErrorException() {
	}

	public PassWordErrorException(String message) {
		super(message);
	}

	public PassWordErrorException(Throwable cause) {
		super(cause);
	}

	public PassWordErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
