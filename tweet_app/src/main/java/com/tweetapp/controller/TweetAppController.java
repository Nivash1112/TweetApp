package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.TweetDb;
import com.tweetapp.model.TweetReplayRequest;
import com.tweetapp.model.UpdateTweetRequest;
import com.tweetapp.model.User;
import com.tweetapp.service.TweetAppService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {

	@Autowired
	TweetAppService tweetAppService;

	@GetMapping("/all")
	public ResponseEntity<?> getAllTweets() {
		List<TweetDb> listTweets = tweetAppService.getAllTweet();
		return new ResponseEntity<>(listTweets, HttpStatus.OK);
	}

	@PostMapping("/{userName}/add")
	public ResponseEntity<?> addTweet(@PathVariable(value = "userName") String userName, @RequestBody TweetDb tweetDb) {
		TweetDb tweetResponse = tweetAppService.addTweet(tweetDb);
		return new ResponseEntity<>(tweetResponse, HttpStatus.CREATED);
	}

	@GetMapping("/user/all")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> listUsers = tweetAppService.getAllUsers();
		return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}

	@GetMapping("/user/search/{userName}")
	public ResponseEntity<User> getUserByUserName(@PathVariable(value = "userName") String userName) {
		User user = tweetAppService.getUserByUserName(userName);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/{userName}")
	public ResponseEntity<List<TweetDb>> getTweetByUserName(@PathVariable(value = "userName") String userName) {
		List<TweetDb> listUserTweets = tweetAppService.getTweetByUserName(userName);
		if (listUserTweets.size() > 0) {
			return new ResponseEntity<>(listUserTweets, HttpStatus.OK);
		} else {
			throw new TweetNotFoundException("Tweets not found...!");
		}
	}

	@PutMapping("/{userName}/update/{tweetId}")
	public ResponseEntity<?> updateTweetByUserNameAndTweetId(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId, @RequestBody UpdateTweetRequest updateTweetRequest) {
		if(userName.equals(updateTweetRequest.getUserName())&& tweetId==updateTweetRequest.getTweetId()) {
		TweetDb tweet = tweetAppService.updateTweetByUserNameAndTweetId(userName, tweetId, updateTweetRequest);
		return new ResponseEntity<>(tweet, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Please verify data given...!", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{userName}/delete/{tweetId}")
	public ResponseEntity<String> deleteTweetByTweetID(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId) {
		String response = tweetAppService.deleteTweetByTweetIdAndUserName(tweetId, userName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{userName}/like/{tweetId}")
	public ResponseEntity<TweetDb> likeTweetByTweetIdAndUserName(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId) {
		TweetDb tweet = tweetAppService.likeTweetByTweetIdAndUserName(tweetId);
		return new ResponseEntity<>(tweet, HttpStatus.OK);
	}

	@PutMapping("{userName}/reply/{tweetId}")
	public ResponseEntity<?> replayToTweetByTweet(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId, @RequestBody TweetReplayRequest tweetReplayRequest) {
		if(tweetId==tweetReplayRequest.getTweetId()) {
		TweetDb tweet = tweetAppService.replayToTweetByTweet(tweetId, tweetReplayRequest);
		return new ResponseEntity<>(tweet, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("check Id...!", HttpStatus.BAD_REQUEST);
		}
	}

}
