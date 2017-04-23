package edu.brown.cs.ndemarco.brownapi.Dining;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import edu.brown.cs.ndemarco.josiah.apiaiUtil.ApiAiDate;

public class Request {

	/**
	 * URLs for cafebonappetit
	 */
	@SuppressWarnings("unused")
	private static final String CAFES_ENDPOINT = "http://legacy.cafebonappetit.com/api/2/cafes";
	private static final String MENUS_ENDPOINT = "http://legacy.cafebonappetit.com/api/2/menus";
	private static final String ITEMS_ENDPOINT = "http://legacy.cafebonappetit.com/api/2/items";
	private static final String CAFE_ID_PARAMETER = "cafe";
	private static final String DATE_PARAMETER = "date";
	private static final String ITEM_PARAMETER = "item";
	private static final String DAYPART_PARAMETER = "daypart";

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private DININGHALL diningHall;
	private Date date;
	private String itemId;
	private List<MEALTIME> mealTimes;

	private Request(Builder dqb) {
		this.diningHall = dqb.diningHall;
		this.date = dqb.date;
		this.itemId = dqb.itemId;
		this.mealTimes = dqb.mealTimes;

	}

	private Response execute() throws UserFriendlyException {
		if (itemId != null && diningHall != null) {
			// choose not to throw UserFriendly because this should not bubble up to user.
			throw new IllegalArgumentException(
					"Tried to get item and dining hall data in the same request. This is invalid.");
		}

		if (itemId == null && diningHall == null) {
			return Response.emptyResponse();
		}

		// Create the Url with parameters for our request
		GenericUrl diningUrl = createUrl();

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
			HttpRequest request = requestFactory.buildGetRequest(diningUrl);
			// Execute and parse the response. May throw IOException.
			response = request.execute().parseAs(Response.class);
		} catch (IOException e) {
			// TODO disconnect Josiah functionality (UserFriendly) from the API.
			throw new UserFriendlyException(e, UserFriendly.OTHER_SERVICE_FAILED);
		}
		
		if (response.stations().isEmpty()) { 
			throw new UserFriendlyException(null, UserFriendly.EMPTY_DINING_RESPONSE);
		}

		for (Station station : response.stations()) {
			for (String itemId : station.itemIds()) {
				// We need to manually set references to stations
				// for each item.
				response.items().get(itemId).station(station);
				// Also, since stations only get item Ids, it's nice
				// to have hard references to these objects in memory
				station.addItem(response.items().get(itemId));
			}
		}

		// Clean up the labels for stations
		for (Map.Entry<String, Item> entry : response.items().entrySet()) {
			String label = entry.getValue().station().label();
			label = Clean.removeHtmlTags(label);
			label = Clean.removeLeadingAtSign(label);
			entry.getValue().station().label(label);
		}
		return response;
	}

	private GenericUrl createUrl() {
		GenericUrl diningUrl;

		if (diningHall != null) {
			diningUrl = menuUrl();
		} else { // itemId != null
			diningUrl = itemUrl();
		}

		diningUrl.set("format", "json");
		return diningUrl;
	}

	private GenericUrl menuUrl() {
		GenericUrl diningUrl = new GenericUrl(MENUS_ENDPOINT);
		diningUrl.set(CAFE_ID_PARAMETER, diningHall.getId());
		if (date != null) {
			diningUrl.set(DATE_PARAMETER, DATE_FORMAT.format(date));
		}

		if (mealTimes != null) {
			StringBuilder sb = new StringBuilder();
			for (MEALTIME mt : mealTimes) {
				sb.append(String.format("%d,", mt.getId()));
			}
			diningUrl.set(DAYPART_PARAMETER, sb.toString());
		}
		return diningUrl;
	}

	private GenericUrl itemUrl() {
		GenericUrl diningUrl = new GenericUrl(ITEMS_ENDPOINT);
		diningUrl.set(ITEM_PARAMETER, itemId);
		return diningUrl;
	}

	// MARK: Builder

	public static class Builder {
		private DININGHALL diningHall;
		private Date date;
		private String itemId;
		private List<MEALTIME> mealTimes;

		public Builder withDiningHall(DININGHALL d) {
			this.diningHall = d;
			return this;
		}

		public Builder withDate(Date d) {
			this.date = d;
			return this;
		}

		public Builder withDate(String dateString) {
			if (dateString != null && !dateString.equals("")) {
				this.date = ApiAiDate.parse(dateString);
			}
			return this;
		}

		public Builder withItemId(String id) {
			this.itemId = id;
			return this;
		}

		public Builder withMealTime(MEALTIME mt) {
			if (this.mealTimes == null) {
				this.mealTimes = new ArrayList<>();
			}
			this.mealTimes.add(mt);
			return this;
		}

		public Builder withMealTimes(List<MEALTIME> list) {
			this.mealTimes = list;
			return this;
		}

		public Response execute() throws UserFriendlyException {
			return new Request(this).execute();
		}
	}
}
