package com.rsi.kino.client.exception;

public class ObjectNotFoundException extends Exception {
	
	private String message;

	public ObjectNotFoundException( String message) {
		super(message);
	}

}
