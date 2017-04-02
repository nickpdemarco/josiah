package edu.brown.cs.ndemarco.brownapi.office;

public class Authorization {

	// Credentials are immutable.
	private final String username;
	private final String password;
	
	private Authorization(Builder b) {
		this.username = b.username;
		this.password = b.password;
	}
	
	// Credentials are only visible within the package.
	String getUsername() {
		return username;
	}
	
	
	String getPassword() {
		return password;
	}
	
	public static class Builder {
		private String username;
		private String password;
		
		public Builder withUsername(String username) {
			this.username = username;
			return this;
		}
		
		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}
		
		public Authorization build() {
			return new Authorization(this);
		}
	}
}
