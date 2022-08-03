package com.tweetapp.model;

public class TweetLoginResponse {
	
	private final String jwt;
	
	public TweetLoginResponse(String jwt) {
		this.jwt=jwt;
	}
	
	public String getJwt() {
		return jwt;
	}

}
