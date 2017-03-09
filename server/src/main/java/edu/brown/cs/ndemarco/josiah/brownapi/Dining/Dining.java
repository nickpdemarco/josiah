package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;


public class Dining {

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
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd");

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private DINING_HALL diningHall;
	private Date date;
	private String itemId;
	private List<MEAL_TIME> mealTimes;

	private Dining(QueryBuilder dqb) {
		this.diningHall = dqb.diningHall;
		this.date = dqb.date;
		this.itemId = dqb.itemId;
		this.mealTimes = dqb.mealTimes;

	}
	
	private Response execute() {
		if (itemId != null && diningHall != null) {
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
			// Parse the response
		    response = request.execute().parseAs(Response.class);
		} catch (IOException e) {
			// TODO update this
			e.printStackTrace();
			return Response.emptyResponse();
		}
				
		// The hyper-nested loop! Don't be scared - because this is only
		// iterating over the deserialized json object, it may look gross,
		// but is actually fairly quick. 
		// Most of these fields have one element in them for basic queries.
		for (Day day : response.days()) {
			for (CafeSummary cafesummary : day.cafes().values()) {
				for (List<Daypart> daypartList : cafesummary.dayparts()) {
					for (Daypart daypart : daypartList) {
						for (Station station : daypart.stations()) {
							for (String itemId : station.items()) {
								// We need to manually set references to stations
								// for each item.
								response.items().get(itemId).station(station);
							}
						}
					}
				}
			}
		}		
		
		// Strip some HTML tags from the output.
		for (Map.Entry<String, Item> entry : response.items().entrySet()) {
			String label = entry.getValue().station().label();
			label = Jsoup.parse(label).text();
			// Also remove the '@' reference tags provided by the API.
			if (label.length() > 0 && label.charAt(0) == '@') {
				label = label.substring(1, label.length());
			}
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
			for (MEAL_TIME mt : mealTimes) {
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

	public static class QueryBuilder {
		private DINING_HALL diningHall;
		private Date date;
		private String itemId;
		private List<MEAL_TIME> mealTimes;

		public QueryBuilder withDiningHall(DINING_HALL d) {
			this.diningHall = d;
			return this;
		}

		public QueryBuilder withDate(Date d) {
			this.date = d;
			return this;
		}
		
		public QueryBuilder withDate(String dateString) {
			if (dateString != null && !dateString.equals("")) {
				try {
					this.date = DATE_FORMAT.parse(dateString);
				} catch (ParseException e) {
					System.out.println("ERROR: Parse exception while making date string. Did API.ai change their format?");
				}
			}
			return this;
		}

		public QueryBuilder withItemId(String id) {
			this.itemId = id;
			return this;
		}
		
		public QueryBuilder withMealTime(MEAL_TIME mt) {
			if (this.mealTimes == null) {
				this.mealTimes = new ArrayList<>();
			}
			this.mealTimes.add(mt);
			return this;
		}
		
		public QueryBuilder withMealTimes(List<MEAL_TIME> list) {
			this.mealTimes = list;
			return this;
		}

		public Response execute() {
			return new Dining(this).execute();
		}
	}
}
