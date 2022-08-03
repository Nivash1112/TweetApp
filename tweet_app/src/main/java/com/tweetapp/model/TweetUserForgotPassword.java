package com.tweetapp.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetUserForgotPassword {
	@NotNull(message = "User name is required")
	private String userName;
	
	@NotNull(message = "Password is required")
	private String newPassword;
	
	@NotNull(message = "Password is required")
	private String confirmPassword;

}
