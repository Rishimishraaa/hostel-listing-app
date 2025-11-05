package com.rk.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.rk.exception.AppException;

@RestControllerAdvice
public class GlobleExceptionHandler {
	
	@ExceptionHandler(AppException.class)
	public ResponseEntity<Map<String,String>> handleAppException(AppException ex){
		Map<String, String> error = new HashMap<>();
		error.put("message", ex.getMessage());
		System.out.println("error is "+ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e){
		Map<String, String> error = new HashMap<>();
		error.put("message", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}

	
	
}
