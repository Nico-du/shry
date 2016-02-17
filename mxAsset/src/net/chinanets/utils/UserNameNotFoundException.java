package net.chinanets.utils;

import org.apache.log4j.Logger;

public class UserNameNotFoundException extends Exception {

	
	public UserNameNotFoundException() {

	}

	public UserNameNotFoundException(String message) {
		super(message);
	}

	public UserNameNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNameNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
