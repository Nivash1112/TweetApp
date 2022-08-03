package com.tweetapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

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

@ExtendWith(MockitoExtension.class)
public class TweetAppServiceTest {

	@InjectMocks
	TweetAppService tweetAppService;

	@Mock
	TweetDbRepo tweetDbRepo;

	@Mock
	UserDbRepo userDbRepo;

	@Mock
	RegistrationDbRepo registrationDbRepo;

	@Mock
	KafKaProducerService kafkaProducer;

	RegistrationDb registrationDb;

	RegistrationDb registrationDb2;

	TweetDb tweetDb;

	TweetDb tweetDb2;

	User user;

	@BeforeEach
	public void init() {
		registrationDb = new RegistrationDb();
		registrationDb.setFirstName("test-first");
		registrationDb.setLastName("test-last");
		registrationDb.setUserName("test-u1");
		registrationDb.setPassword("test-pass");
		registrationDb.setConfirmPassword("test-pass");
		registrationDb.setContactNumber("1234567890");
		registrationDb.setEmail("test@test.com");

		registrationDb2 = new RegistrationDb();
		registrationDb2.setFirstName("test-first");
		registrationDb2.setLastName("test-last");
		registrationDb2.setUserName("test-u1");
		registrationDb2.setPassword("test-pass");
		registrationDb2.setConfirmPassword("test-pass2");
		registrationDb2.setContactNumber("1234567890");
		registrationDb2.setEmail("test@test.com");

		user = new User();
		user.setFirstName(registrationDb.getFirstName());
		user.setLastName(registrationDb.getLastName());
		user.setEmailId(registrationDb.getEmail());
		user.setUserName(registrationDb.getUserName());
		user.setContactNumber(registrationDb.getContactNumber());

		tweetDb = new TweetDb();
		tweetDb.setUserName("test-u1");
		tweetDb.setTweetId(1001);
		tweetDb.setLike(1);
		tweetDb.setTweet("test-tweet");
		tweetDb.setTweetTag("tag");
		List<String> reply = new ArrayList<>();
		reply.add("test-reply");
		tweetDb.setTweetReplay(reply);

		tweetDb2 = new TweetDb();
		tweetDb2.setUserName("test-u");
		tweetDb2.setTweetId(1001);
		tweetDb2.setLike(1);
		tweetDb2.setTweet("test-tweet");
		tweetDb2.setTweetTag("tag");
	}

	@Test
	public void addUserHappyPath() throws Exception {
		when(registrationDbRepo.findByUserNameOrEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
				.thenReturn(new ArrayList<>());
		when(registrationDbRepo.save(registrationDb)).thenReturn(null);
		when(userDbRepo.save(ArgumentMatchers.any())).thenReturn(user);
		assertEquals(registrationDb.getEmail(), tweetAppService.addUser(registrationDb).getEmailId());

	}

	@Test
	public void addUserExceptionTest() throws Exception {
		when(registrationDbRepo.findByUserNameOrEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
				.thenReturn(new ArrayList<>());
		assertThrows(Exception.class, () -> tweetAppService.addUser(registrationDb2));
	}

	@Test
	public void addUser() throws Exception {
		List<RegistrationDb> listReg = new ArrayList<>();
		listReg.add(registrationDb);
		listReg.add(new RegistrationDb());
		when(registrationDbRepo.findByUserNameOrEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
				.thenReturn(listReg);
		assertThrows(UserAlreadyExistsException.class, () -> tweetAppService.addUser(registrationDb).getEmailId());
	}

	@Test
	public void forgotPasswordTest() {
		TweetUserForgotPassword forgotPassword = new TweetUserForgotPassword();
		forgotPassword.setUserName(registrationDb.getUserName());
		forgotPassword.setNewPassword(registrationDb.getPassword());
		forgotPassword.setConfirmPassword(registrationDb.getConfirmPassword());
		when(registrationDbRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(registrationDb);
		when(registrationDbRepo.save(ArgumentMatchers.any())).thenReturn(null);
		assertNotNull(tweetAppService.forgotPassword(forgotPassword));
	}

	@Test
	public void getAllTweetHappyPathTest() {
		List<TweetDb> listTweet = new ArrayList<TweetDb>();
		listTweet.add(tweetDb);
		when(tweetDbRepo.findAll()).thenReturn(listTweet);
		assertEquals(tweetDb.getUserName(), tweetAppService.getAllTweet().get(0).getUserName());
	}

	@Test
	public void addTweetHappyPath() {
		when(userDbRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(user);
		when(tweetDbRepo.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getTweetTag(), tweetAppService.addTweet(tweetDb).getTweetTag());
	}

	@Test
	public void addTweetTest() {
		when(userDbRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(user);
		assertThrows(UserNotFoundException.class, () -> tweetAppService.addTweet(tweetDb2));
	}

	@Test
	public void getAllUsersHappyPath() {
		List<User> listUser = new ArrayList<>();
		listUser.add(user);
		when(userDbRepo.findAll()).thenReturn(listUser);
		assertEquals(user.getContactNumber(), tweetAppService.getAllUsers().get(0).getContactNumber());
	}

	@Test
	public void getUserByUserNameHappyPath() {
		when(userDbRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(user);
		assertEquals(user.getFirstName(), tweetAppService.getUserByUserName(user.getUserName()).getFirstName());
	}

	@Test
	public void getTweetByUserNameHappyPath() {
		List<TweetDb> listTweet = new ArrayList<TweetDb>();
		listTweet.add(tweetDb);
		when(tweetDbRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(listTweet);
		assertEquals(tweetDb.getTweetId(),
				tweetAppService.getTweetByUserName(tweetDb.getUserName()).get(0).getTweetId());
	}

	@Test
	public void updateTweetByUserNameAndTweetIdHappyPath() {
		UpdateTweetRequest updateTweetRequest = new UpdateTweetRequest();
		updateTweetRequest.setTweetId(tweetDb.getTweetId());
		updateTweetRequest.setUserName(tweetDb.getUserName());
		updateTweetRequest.setTweet(tweetDb.getTweet());
		when(tweetDbRepo.getTweetDbByTweetIdAndUserName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(tweetDb);
		when(tweetDbRepo.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getLike(), tweetAppService
				.updateTweetByUserNameAndTweetId(tweetDb.getUserName(), tweetDb.getTweetId(), updateTweetRequest)
				.getLike());
	}

	@Test
	public void deleteTweetByTweetIdAndUserNameHappyPath() {
		when(tweetDbRepo.getTweetDbByTweetIdAndUserName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(tweetDb);
		assertNotNull(tweetAppService.deleteTweetByTweetIdAndUserName(tweetDb.getTweetId(), tweetDb.getUserName()));
	}

	@Test
	public void deleteTweetByTweetIdAndUserName() {
		when(tweetDbRepo.getTweetDbByTweetIdAndUserName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(null);
		assertThrows(TweetNotFoundException.class,
				() -> tweetAppService.deleteTweetByTweetIdAndUserName(tweetDb.getTweetId(), tweetDb.getUserName()));
	}

	@Test
	public void likeTweetByTweetIdAndUserNameHappyPath() {
		when(tweetDbRepo.findByTweetId(ArgumentMatchers.anyInt())).thenReturn(tweetDb);
		when(tweetDbRepo.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getTweetReplay(),
				tweetAppService.likeTweetByTweetIdAndUserName(tweetDb.getTweetId()).getTweetReplay());
	}

	@Test
	public void replayToTweetByTweetHappyPath() {
		TweetReplayRequest replayRequest = new TweetReplayRequest();
		replayRequest.setTweetId(tweetDb.getTweetId());
		replayRequest.setTweetReplay("tweet");
		when(tweetDbRepo.findByTweetId(ArgumentMatchers.anyInt())).thenReturn(tweetDb);
		when(tweetDbRepo.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getLike(),
				tweetAppService.replayToTweetByTweet(tweetDb.getTweetId(), replayRequest).getLike());
	}
}
