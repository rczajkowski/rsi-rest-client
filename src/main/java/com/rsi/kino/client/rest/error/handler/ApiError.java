package com.rsi.kino.client.rest.error.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiError {
	HttpStatus status;
	int errorCode;
	String message;
	String exceptionClassName;
}
