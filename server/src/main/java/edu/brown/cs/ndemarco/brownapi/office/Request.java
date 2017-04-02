package edu.brown.cs.ndemarco.brownapi.office;

public class Request {
	
	// Note that we leave a %s token in here to append the username:password.
	// The final format for the query should be:
	// "https://USERNAME:PASSWORD@esb.brown.edu/services/cis/faculty-lookup-api/v1/faculty";
	private static final String ENDPOINT = "https://%sesb.brown.edu/services/cis/faculty-lookup-api/v1/faculty"; 
	
	private Authorization auth;
	
	private Request(Builder b) {
		this.auth = b.auth;
	}
	
	private Response execute() {
		return new Response();
	}
	
	
	public static class Builder {
		private Authorization auth;
		
		public Builder withAuthorization(Authorization auth) {
			this.auth = auth;
			return this;
		}
		
		public Response execute() {
			return new Request(this).execute();
		}
	}
}
