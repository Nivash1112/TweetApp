package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tweetapp.model.RegistrationDb;
import com.tweetapp.model.TweetUserForgotPassword;
import com.tweetapp.model.User;
import com.tweetapp.service.TweetAppService;

@ExtendWith(MockitoExtension.class)
public class LoginManagerControllerTest {
	
	@InjectMocks
	LoginManagerController loginManager;
	
	@Mock
	TweetAppService tweetAppService;

	@Test
	public void registerUserTest() throws Exception {
		RegistrationDb register=new RegistrationDb();
		when(tweetAppService.addUser(ArgumentMatchers.any())).thenReturn(new User());
		assertEquals(201, loginManager.registerUser(register).getStatusCodeValue());
	}
	
	@Test
	public void forgotPasswordHappyPathTest() {
		TweetUserForgotPassword forgotPassword=new TweetUserForgotPassword();
		forgotPassword.setUserName("test-user1");
		forgotPassword.setNewPassword("test-pass");
		forgotPassword.setConfirmPassword("test-pass");
		assertEquals(200, loginManager.forgotPassword(forgotPassword).getStatusCodeValue());
	}
	
	@Test
	public void forgotPassword404Test() {
		TweetUserForgotPassword forgotPassword=new TweetUserForgotPassword();
		forgotPassword.setUserName("test-user1");
		forgotPassword.setNewPassword("test-pass");
		forgotPassword.setConfirmPassword("test");
		assertEquals(400, loginManager.forgotPassword(forgotPassword).getStatusCodeValue());
	}
}
