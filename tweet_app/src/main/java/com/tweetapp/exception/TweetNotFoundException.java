package com.tweetapp.exception;

public class TweetNotFoundException extends RuntimeException {

	/**
	 * Tweet Not Found Exception
	 */
	private static final long serialVersionUID = 1L;
	public TweetNotFoundException(String msg) {
		super(msg);
	}

}