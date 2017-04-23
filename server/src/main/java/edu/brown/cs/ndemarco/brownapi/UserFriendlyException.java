package edu.brown.cs.ndemarco.brownapi;


public class UserFriendlyException extends Exception {

	/**
	 * AUTO GENERATED 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Exception exception;
	private String userFriendlyDescription;
	
	public UserFriendlyException(Exception e, String userFriendlyDescription) {
		if (userFriendlyDescription == null) {
			throw new IllegalArgumentException("userFriendlyDescription parameter cannot be null.");
		}
		
		this.exception = e; // can be null.
		this.userFriendlyDescription = userFriendlyDescription;
		
	}
	
	public Exception originalException() {
		return exception;
	}
	
	public String userFriendlyDescription() {
		return userFriendlyDescription;
	}
}
