package com.tweetapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTweetRequest {
	
	private String userName;
	
	private int tweetId;
	
	private String tweet;

}
