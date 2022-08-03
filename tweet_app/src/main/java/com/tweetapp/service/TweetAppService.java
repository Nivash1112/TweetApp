package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.exception.UserAlreadyExistsException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.RegistrationDb;
import com.tweetapp.model.TweetDb;
import com.tweetapp.model.TweetReplayRequest;
import com.tweetapp.model.TweetUserForgotPassword;
import com.tweetapp.model.UpdateTweetRequest;
import com.tweetapp.model.User;
import com.tweetapp.repository.RegistrationDbRepo;
import com.tweetapp.repository.TweetDbRepo;
import com.tweetapp.repository.UserDbRepo;

@Service
public class TweetAppService {

	@Autowired
	TweetDbRepo tweetDbRepo;

	@Autowired
	UserDbRepo userDbRepo;

	@Autowired
	RegistrationDbRepo registrationDbRepo;

	@Autowired
	KafKaProducerService kafkaProducer;

	private static final Logger log = LoggerFactory.getLogger(TweetAppService.class);

	public User addUser(RegistrationDb registrationDb) throws Exception {
		if (registrationDbRepo.findByUserNameOrEmail(registrationDb.getUserName(), registrationDb.getEmail())
				.isEmpty()) {
			if (registrationDb.getPassword().equals(registrationDb.getConfirmPassword())) {
				User user = addObjectToUserFromRegistration(registrationDb);
				registrationDbRepo.save(registrationDb);
				kafkaProducer.sendMessage("User created...");
				return userDbRepo.save(user);
			} else {
				throw new Exception("New Password and confirm password must be same..!");
			}
		} else {
			throw new UserAlreadyExistsException("User Already exists so you cannot add this user");
		}
	}

	public User addObjectToUserFromRegistration(RegistrationDb registrationDb) {
		User user = new User();
		user.setUserName(registrationDb.getUserName());
		user.setFirstName(registrationDb.getFirstName());
		user.setLastName(registrationDb.getLastName());
		user.setEmailId(registrationDb.getEmail());
		user.setContactNumber(registrationDb.getContactNumber());
		return user;
	}

	public String forgotPassword(TweetUserForgotPassword forgotPassword) {
		RegistrationDb registrationDb = registrationDbRepo.findByUserName(forgotPassword.getUserName());
		registrationDb.setPassword(forgotPassword.getNewPassword());
		registrationDb.setConfirmPassword(forgotPassword.getConfirmPassword());
		registrationDbRepo.save(registrationDb);
		kafkaProducer.sendMessage("password changed successfully...!");
		return "password changed successfully...!";
	}

	public List<TweetDb> getAllTweet() {
		List<TweetDb> listTweets = null;
		try {
			listTweets = tweetDbRepo.findAll();
			kafkaProducer.sendMessage("Get all fetch " + listTweets.size() + " data from DB");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return listTweets;
	}

	public TweetDb addTweet(TweetDb tweetDb) {
		if (tweetDb.getTweetReplay() == null) {
			tweetDb.setTweetReplay(new ArrayList<>());
		}
		if (tweetDb.getUserName().equals(userDbRepo.findByUserName(tweetDb.getUserName()).getUserName())) {
			return tweetDbRepo.save(tweetDb);
		} else {
			throw new UserNotFoundException("Give Valid User name...!");
		}
	}

	public List<User> getAllUsers() {
		return userDbRepo.findAll();
	}

	public User getUserByUserName(String userName) {
		return userDbRepo.findByUserName(userName);
	}

	public List<TweetDb> getTweetByUserName(String userName) {
		return tweetDbRepo.findByUserName(userName);

	}

	public TweetDb updateTweetByUserNameAndTweetId(String userName, int tweetId,
			UpdateTweetRequest updateTweetRequest) {
		TweetDb tweet = tweetDbRepo.getTweetDbByTweetIdAndUserName(tweetId, userName);
		tweet.setTweet(updateTweetRequest.getTweet());
		return tweetDbRepo.save(tweet);
	}

	public String deleteTweetByTweetIdAndUserName(int tweetId, String userName) {
		if (tweetDbRepo.getTweetDbByTweetIdAndUserName(tweetId, userName) != null) {
			tweetDbRepo.deleteTweetDbByTweetIdAndUserName(tweetId, userName);
			return "successfully deleted...!";
		} else {
			throw new TweetNotFoundException("Tweet not found...!");
		}
	}

	public TweetDb likeTweetByTweetIdAndUserName(int tweetId) {
		TweetDb tweet = tweetDbRepo.findByTweetId(tweetId);
		tweet.setLike(tweet.getLike() + 1);
		return tweetDbRepo.save(tweet);
	}

	public TweetDb replayToTweetByTweet(int tweetId, TweetReplayRequest replayRequest) {
		TweetDb tweet = tweetDbRepo.findByTweetId(tweetId);
		List<String> tweetReplay = tweet.getTweetReplay();
		tweetReplay.add(replayRequest.getTweetReplay());
		tweet.setTweetReplay(tweetReplay);
		return tweetDbRepo.save(tweet);
	}

}
