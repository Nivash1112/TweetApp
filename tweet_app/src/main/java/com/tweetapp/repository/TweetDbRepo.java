package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tweetapp.model.TweetDb;

public interface TweetDbRepo extends MongoRepository<TweetDb, String> {

	List<TweetDb> findByUserName(String userName);

	void deleteTweetDbByTweetIdAndUserName(int id, String userName);

	TweetDb findByTweetId(@Param("tweetId") int tweetId);

	TweetDb getTweetDbByTweetIdAndUserName(int tweetId, String userName);
	

}
