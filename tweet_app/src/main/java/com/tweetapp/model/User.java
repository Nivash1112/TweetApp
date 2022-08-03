package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class User {

	@Id
	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	private String emailId;
	
	
	private String contactNumber;
}
