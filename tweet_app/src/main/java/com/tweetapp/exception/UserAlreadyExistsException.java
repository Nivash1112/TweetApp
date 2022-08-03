package com.tweetapp.exception;
public class UserAlreadyExistsException extends RuntimeException {

	/**
	 * User Already Exists Exception
	 */
	private static final long serialVersionUID = 1L;
	public UserAlreadyExistsException(String msg) {
		super(msg);
	}

}