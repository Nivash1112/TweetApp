package com.tweetapp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.model.RegistrationDb;
import com.tweetapp.repository.RegistrationDbRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	RegistrationDbRepo registrationDbRepo;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		RegistrationDb registrationDb=registrationDbRepo.findByUserName(userName);
		return new User(registrationDb.getUserName(), registrationDb.getPassword(), new ArrayList<>());
	}
}
