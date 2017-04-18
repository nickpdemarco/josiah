package edu.brown.cs.ndemarco.brownapi.office;

import java.io.IOException;

import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import edu.brown.cs.ndemarco.brownapi.UserFriendlyException;
import edu.brown.cs.ndemarco.josiah.UserFriendly;

public class Request {

	// Note that we leave a %s token in here to append the username:password.
	// The final format for the query should be:
	// "https://USERNAME:PASSWORD@esb.brown.edu/services/cis/faculty-lookup-api/v1/faculty";
	private static final String ENDPOINT = "https://esb.brown.edu/services/cis/faculty-lookup-api/v1/faculty";
	private static final String FIRST_NAME_PARAMETER = "first_name";
	private static final String LAST_NAME_PARAMETER = "last_name";
	private static final String BROWN_ID_PARAMETER = "brown_id";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	// At the moment, the builder behaves like a data bean, so we might as well
	// just save a reference to it.
	private Builder builder;

	private Request(Builder b) {
		this.builder = b;
	}

	private Response execute() throws UserFriendlyException {
		GenericUrl url = createUrl();

		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			// TODO this will fail if auth fields are null. Needs refactor.
			private final BasicAuthentication auth = new BasicAuthentication(builder.auth.getUsername(),
					builder.auth.getPassword());

			@Override
			public void initialize(HttpRequest request) {
				// Using preemptive HTTP authentication to avoid this error :
				// https://support.mulesoft.com/s/article/ka4340000004HKEAA2/Registered-authentication-is-set-to-HttpBasicAuthenticationFilter-but-there-was-no-security-context
				request.setInterceptor(auth);
			}
		});

		Response response = Response.emptyResponse();

		try {
			// Make the request
			HttpRequest request = requestFactory.buildGetRequest(url);
			// Execute and parse the response. May throw IOException.
			HttpResponse http = request.execute();
			response = ResponseStreamDecoder.decode(http);
		} catch (IOException e) {
			// TODO disconnect Josiah functionality (UserFriendly) from the API.
			System.out.println(e.getMessage());
			throw new UserFriendlyException(e, UserFriendly.OTHER_SERVICE_FAILED);
		}

		return response;
	}

	// Simply append the builder's parameters to the url.
	private GenericUrl createUrl() {
		GenericUrl url = new GenericUrl(ENDPOINT);

		// Append credentials. Credentials _might_ be null, because it's
		// conceivable that
		// future versions of the API might have valid queries that do not
		// require authorization.
		// As of this writing, this is a de-facto required field, however. We
		// delegate dealing with
		// unauthorized requests at the time of execution: the API returns an
		// error indicating an auth issue,
		// and we throw an exception as a result.
		if (builder.auth != null) {
			url.setUserInfo(builder.auth.toString());
		}

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

		public Builder withFirstNamePrefix(String firstNamePrefix) {
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
