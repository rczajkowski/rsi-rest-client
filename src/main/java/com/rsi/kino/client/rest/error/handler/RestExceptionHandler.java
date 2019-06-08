package com.rsi.kino.client.rest.error.handler;

import com.rsi.kino.client.exception.ObjectAlreadyExistsException;
import com.rsi.kino.client.exception.ObjectNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return buildResponseEntity(new ApiError(status, status.value(), ex.getMessage(), ex.getClass().getName()));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	protected ResponseEntity<Object> handleException(
			ObjectNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex.getClass().getName());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(ObjectAlreadyExistsException.class)
	protected ResponseEntity<Object> handleException(
			ObjectAlreadyExistsException ex) {
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), ex.getMessage(), ex.getClass().getName());
		return buildResponseEntity(apiError);
	}

	//other exception handlers below

}