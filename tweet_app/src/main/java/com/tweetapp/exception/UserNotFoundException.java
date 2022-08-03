package com.tweetapp.exception;
public class UserNotFoundException extends RuntimeException {
	/**
	 * User Not Found Exception
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String msg) {
		super(msg);
	}

}