package edu.brown.cs.ndemarco.brownapi.office;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import edu.brown.cs.ndemarco.brownapi.UserFriendlyException;
import edu.brown.cs.ndemarco.josiah.UserFriendly;

public class Request {
	
	// Note that we leave a %s token in here to append the username:password.
	// The final format for the query should be:
	// "https://USERNAME:PASSWORD@esb.brown.edu/services/cis/faculty-lookup-api/v1/faculty";
	private static final String ENDPOINT = "https://%sesb.brown.edu/services/cis/faculty-lookup-api/v1/faculty"; 
	private static final String FIRST_NAME_PARAMETER = "first_name";
	private static final String LAST_NAME_PARAMETER = "last_name";
	private static final String BROWN_ID_PARAMETER = "brown_id";
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	// At the moment, the builder behaves like a data bean, so we might as well just save a reference to it.
	private Builder builder; 
	
	private Request(Builder b) {
		this.builder = b;
	}
	
	private Response execute() throws UserFriendlyException {
		GenericUrl url = createUrl();
		// Set up the request factory
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
		
		Response response = Response.emptyResponse();
		
		try {
			// Make the request
			HttpRequest request = requestFactory.buildGetRequest(url);
			// Execute and parse the response. May throw IOException.
			response = request.execute().parseAs(Response.class);
		} catch (IOException e) {
			// TODO disconnect Josiah functionality (UserFriendly) from the API.
			throw new UserFriendlyException(e, UserFriendly.OTHER_SERVICE_FAILED);
		}
		
		if (response.failed()) {
			throw new RuntimeException(response.errorMessage());
		}
		
		return response;
	}
	
	
	// Simply append the builder's parameters to the url.
	private GenericUrl createUrl() {
		// Append credentials. Credentials _might_ be null, because it's conceivable that
		// future versions of the API might have valid queries that do not require authorization.
		// As of this writing, this is a de-facto required field, however. We delegate dealing with 
		// unauthorized requests at the time of execution: the API returns an error indicating an auth issue,
		// and we throw an exception as a result.
		
		String urlStr = String.format(ENDPOINT, (builder.auth == null) ? "" : builder.auth.toString());
		
		GenericUrl url = new GenericUrl(urlStr);
		
		if (builder.brownId != null) {
			url.set(BROWN_ID_PARAMETER, builder.brownId);
		}
		
		if (builder.firstNamePrefix != null) {
			url.set(FIRST_NAME_PARAMETER, builder.firstNamePrefix);
		}
		
		if (builder.lastNamePrefix != null) {
			url.set(LAST_NAME_PARAMETER, builder.lastNamePrefix);
		}
		
		return url;
				
		
	}
	
	
	public static class Builder {
		private Authorization auth;
		private String brownId;
		private String firstNamePrefix;
		private String lastNamePrefix;
		
		public Builder withAuthorization(Authorization auth) {
			this.auth = auth;
			return this;
		}
		
		public Builder withBrownId(String brownId) {
			this.brownId = brownId;
			return this;
		}
		
		public Builder withFirstNamePrefix(String firstNamePrefix){
			this.firstNamePrefix = firstNamePrefix;
			return this;
		}
		
		public Builder withLastNamePrefix(String lastNamePrefix) {
			this.lastNamePrefix = lastNamePrefix;
			return this;
		}
		
		public Response execute() throws UserFriendlyException {
			return new Request(this).execute();
		}
	}
}
