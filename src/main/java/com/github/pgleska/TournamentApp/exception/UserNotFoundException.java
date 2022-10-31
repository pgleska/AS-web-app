package com.github.pgleska.TournamentApp.exception;

public class UserNotFoundException extends Exception {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2956450827483185592L;

	public UserNotFoundException() {
		super();
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
}
