package com.github.pgleska.TournamentApp.exception;

public class UserAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4423923540004471774L;

	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
