package edu.brown.cs.ndemarco.brownapi.office;

import java.util.Objects;

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
	
	@Override
	public String toString() {
		// For convenience, toString returns in the format that the API expects.
		return String.format("%s:%s", username, password);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username, password);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Authorization)) return false;
		Authorization test = (Authorization) o;
		
		return test.username.equals(username) &&
				test.password.equals(password);
		
	}
}
