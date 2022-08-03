package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.TweetDb;
import com.tweetapp.model.TweetReplayRequest;
import com.tweetapp.model.UpdateTweetRequest;
import com.tweetapp.model.User;
import com.tweetapp.service.TweetAppService;

@ExtendWith(MockitoExtension.class)
public class TweetAppControllerTest {

	@InjectMocks
	TweetAppController tweetApp;

	@Mock
	TweetAppService tweetAppService;

	TweetDb tweetDb;

	@BeforeEach
	public void init() {
		tweetDb = new TweetDb();
		tweetDb.setUserName("test-u1");
		tweetDb.setTweetId(1001);
		tweetDb.setLike(1);
		tweetDb.setTweet("test-tweet");
		tweetDb.setTweetTag("tag");
		List<String> reply = new ArrayList<>();
		reply.add("test-reply");
		tweetDb.setTweetReplay(reply);
	}

	@Test
	public void getAllTweetsHappyPathTest() {
		when(tweetAppService.getAllTweet()).thenReturn(new ArrayList<>());
		assertEquals(200, tweetApp.getAllTweets().getStatusCodeValue());
	}

	@Test
	public void addTweetHappyPathTest() {
		when(tweetAppService.addTweet(tweetDb)).thenReturn(tweetDb);
		assertEquals(201, tweetApp.addTweet("test-u1", tweetDb).getStatusCodeValue());
	}

	@Test
	public void getAllUsersHappyPathTest() {
		when(tweetAppService.getAllUsers()).thenReturn(new ArrayList<>());
		assertEquals(200, tweetApp.getAllUsers().getStatusCodeValue());
	}

	@Test
	public void getUserByUserNameHappyPath() {
		when(tweetAppService.getUserByUserName(ArgumentMatchers.any())).thenReturn(new User());
		assertEquals(200, tweetApp.getUserByUserName("test-u1").getStatusCodeValue());
	}

	@Test
	public void getTweetByUserNameHappyPathTest() {
		List<TweetDb> listTweetDb = new ArrayList<>();
		listTweetDb.add(tweetDb);
		when(tweetAppService.getTweetByUserName(ArgumentMatchers.any())).thenReturn(listTweetDb);
		assertEquals(200, tweetApp.getTweetByUserName("test-u1").getStatusCodeValue());
	}

	@Test
	public void getTweetByUserNameTest() {
		List<TweetDb> listTweetDb = new ArrayList<>();
		when(tweetAppService.getTweetByUserName(ArgumentMatchers.any())).thenReturn(listTweetDb);
		assertThrows(TweetNotFoundException.class, () -> tweetApp.getTweetByUserName("test-u1"));
	}

	@Test
	public void updateTweetByUserNameAndTweetIdHappyPathTest() {
		when(tweetAppService.updateTweetByUserNameAndTweetId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
				ArgumentMatchers.any())).thenReturn(tweetDb);
		UpdateTweetRequest updateTweetRequest = new UpdateTweetRequest();
		updateTweetRequest.setUserName(tweetDb.getUserName());
		updateTweetRequest.setTweetId(tweetDb.getTweetId());
		updateTweetRequest.setTweet(tweetDb.getTweet());
		assertEquals(200, tweetApp
				.updateTweetByUserNameAndTweetId(tweetDb.getUserName(), tweetDb.getTweetId(), updateTweetRequest)
				.getStatusCodeValue());
	}
	
	@Test
	public void updateTweetByUserNameAndTweetIdTest() {
		UpdateTweetRequest updateTweetRequest = new UpdateTweetRequest();
		updateTweetRequest.setUserName("test");
		updateTweetRequest.setTweetId(tweetDb.getTweetId());
		updateTweetRequest.setTweet(tweetDb.getTweet());
		assertEquals(400, tweetApp
				.updateTweetByUserNameAndTweetId(tweetDb.getUserName(), tweetDb.getTweetId(), updateTweetRequest)
				.getStatusCodeValue());
	}

	@Test
	public void deleteTweetByTweetIDHappyPath() {
		when(tweetAppService.deleteTweetByTweetIdAndUserName(tweetDb.getTweetId(), tweetDb.getUserName()))
				.thenReturn("Tweet deleted ...!");
		assertEquals(200,
				tweetApp.deleteTweetByTweetID(tweetDb.getUserName(), tweetDb.getTweetId()).getStatusCodeValue());
	}
	
	@Test
	public void likeTweetByTweetIdAndUserNameHappyPath() {
		when(tweetAppService.likeTweetByTweetIdAndUserName(tweetDb.getTweetId())).thenReturn(tweetDb);
		assertEquals(tweetDb.getTweetTag(), tweetApp.likeTweetByTweetIdAndUserName(tweetDb.getUserName(), tweetDb.getTweetId()).getBody().getTweetTag());
	}
	
	@Test
	public void replayToTweetByTweetHappyPathTest() {
		TweetReplayRequest replayRequest=new TweetReplayRequest();
		replayRequest.setTweetId(tweetDb.getTweetId());
		replayRequest.setTweetReplay("replay-test");
		when(tweetAppService.replayToTweetByTweet(tweetDb.getTweetId(), replayRequest)).thenReturn(tweetDb);
		assertEquals(200,tweetApp.replayToTweetByTweet(tweetDb.getUserName(), tweetDb.getTweetId(), replayRequest).getStatusCodeValue());
		
	}
	
	@Test
	public void replayToTweetByTweetTest() {
		TweetReplayRequest replayRequest=new TweetReplayRequest();
		replayRequest.setTweetId(2222);
		replayRequest.setTweetReplay("replay-test");
		assertEquals(400,tweetApp.replayToTweetByTweet(tweetDb.getUserName(), tweetDb.getTweetId(), replayRequest).getStatusCodeValue());
		
	}
}
