package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.model.RegistrationDb;
import com.tweetapp.model.TweetLoginRequest;
import com.tweetapp.model.TweetLoginResponse;
import com.tweetapp.model.TweetUserForgotPassword;
import com.tweetapp.model.User;
import com.tweetapp.repository.RegistrationDbRepo;
import com.tweetapp.service.MyUserDetailsService;
import com.tweetapp.service.TweetAppService;
import com.tweetapp.util.JwtTokenUtil;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class LoginManagerController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	MyUserDetailsService myUserDetailService;

	@Autowired
	TweetAppService tweetAppService;

	@Autowired
	RegistrationDbRepo registrationDbRepo;

	@PostMapping("/authenticate")
	public ResponseEntity<TweetLoginResponse> createAuthenticationToken(@RequestBody TweetLoginRequest loginRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect User-name or password ", e);
		}
		final UserDetails userDetails = myUserDetailService.loadUserByUsername(loginRequest.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new TweetLoginResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody RegistrationDb registrationDb) throws Exception {
		User user = tweetAppService.addUser(registrationDb);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PostMapping("/{userName}/forgot")
	public ResponseEntity<String> forgotPassword(@RequestBody TweetUserForgotPassword forgotPassword) {
		if (forgotPassword.getNewPassword().equals(forgotPassword.getConfirmPassword())) {
		return new ResponseEntity<>(tweetAppService.forgotPassword(forgotPassword), HttpStatus.OK);
		}else {
			return new ResponseEntity<>("New password and confirm password must be same...!", HttpStatus.BAD_REQUEST);
		}
	}
}
