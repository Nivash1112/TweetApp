package com.tweetapp.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tweetapp.model.ErrorDetails;


@RestControllerAdvice
public class TweetAppExceptionHandling {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception exception){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setMsg(exception.getMessage());
		errorDetails.setCode("400");
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setMsg(exception.getMessage());
		errorDetails.setCode("409");
		return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
		
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setMsg(exception.getMessage());
		errorDetails.setCode("404");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(TweetNotFoundException.class)
	public ResponseEntity<?> handleTweetNotFoundException(TweetNotFoundException exception){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setMsg(exception.getMessage());
		errorDetails.setCode("404");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}

}