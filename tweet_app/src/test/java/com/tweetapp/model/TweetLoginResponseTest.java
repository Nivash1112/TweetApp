package com.tweetapp.model;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TweetLoginResponseTest {
	
	TweetLoginResponse tweetLoginResponse;

	@BeforeEach
	public void init(){
		tweetLoginResponse=new TweetLoginResponse("abcd.efgh.ijkl");
	}
	
	@Test
	public void getJwtTest() {
		assertNotNull(tweetLoginResponse.getJwt());
	}
}
