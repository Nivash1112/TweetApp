package com.tweetapp.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class TweetDb {
	@Id
	@NotNull
	private int tweetId;
	
	@Size(max=144)
	@NotNull(message = "tweet is required")
	private String tweet;
	
	@Size(max=50)
	private String tweetTag;
	
	@NotNull(message = "User name field is required")
	private String userName;
	
	private int like;
	
	private List<String> tweetReplay;

}
