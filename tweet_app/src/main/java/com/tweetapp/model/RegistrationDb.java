package com.tweetapp.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class RegistrationDb {
	
	@Id
	@NotNull(message = "user name is required")
	private String userName;

	@NotNull(message="first name is required")
	private String firstName;
	
	@NotNull(message = "last name is required")
	private String lastName;
	
	@NotNull(message = "E-mail is required")
	private String email;
	
	
	@NotNull(message = "Password is required")
	private String password;
	
	@NotNull(message = "confirm password is required")
	private String confirmPassword;
	
	@NotNull(message = "contact number is required")
	private String contactNumber;
	
}
