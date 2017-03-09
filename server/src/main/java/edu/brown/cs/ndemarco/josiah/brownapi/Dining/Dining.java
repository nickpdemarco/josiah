package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jsoup.Jsoup;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.common.collect.Multiset.Entry;


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
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd");

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
	
	private GenericUrl createUrl() {
		GenericUrl diningUrl;

		if (diningHall != null) {
			diningUrl = new GenericUrl(MENUS_ENDPOINT);
			diningUrl.set(CAFE_ID_PARAMETER, diningHall.getId());
			if (date != null) {
				diningUrl.set(DATE_PARAMETER, DATE_FORMAT.format(date));
			}
		} else { // itemId != null
			diningUrl = new GenericUrl(ITEMS_ENDPOINT);
			diningUrl.set(ITEM_PARAMETER, itemId);
		}
		
		if (mealTimes != null) {
			StringBuilder sb = new StringBuilder();
			for (MEAL_TIME mt : mealTimes) {
				sb.append(String.format("%d,", mt.getId()));
			}
			diningUrl.set(DAYPART_PARAMETER, sb.toString());
		}
		
		
		diningUrl.set("format", "json");
		return diningUrl;
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
		
		// Strip some HTML tags from the output.
		for (Map.Entry<String, Item> entry : response.items.entrySet()) {
			String label = entry.getValue().getStation().label();
			label = Jsoup.parse(label).text();
			if (label.length() > 0 && label.charAt(0) == '@') {
				label = label.substring(1, label.length());
			}
			entry.getValue().station = label;
		}
		
		return response;
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

	// MARK: Static classes for GSON parsing:

	public static class Response {
		@Key
		private Day[] days;

		@Key
		private Map<String, Item> items;

		public List<Day> days() {
			return Arrays.asList(days);
		}

		public Map<String, Item> items() {
			return items;
		}
		

		private static Response emptyResponse() {
			Response response = new Response();
			response.items = Collections.emptyMap();
			return response;
		}
	}
	
	public static class Day {
		@Key
		private String date;
		
		@Key
		private Map<String, CafeSummary> cafes;
		
		public String date() {
			return date;
		}
		
		public Map<String, CafeSummary> cafes() {
			return cafes;
		}
	}
	
	public static class CafeSummary {
		@Key
		private String name;
		
		@Key
		private List<List<Daypart>> dayparts;
		
		public String name() {
			return name;
		}
		
		public List<List<Daypart>> dayparts () {
			return dayparts;
		}
	}
	
	public static class Daypart {
		@Key
		private String label;
		
		@Key
		private String starttime;
		
		@Key
		private String endtime;
		
		@Key
		private List<Station> stations;
		
		public String label() {
			return label;
		}
		
		public String startTime() {
			return starttime;
		}
		
		public String endTime() {
			return endtime;
		}
		
		public List<Station> stations() {
			return stations;
		}
	}
	
	public static class Station {
		@Key
		private String label;
		
		@Key
		private List<String> items;
		
		// Principally used by Json parser.
		public Station() {
			this.label = "";
			this.items = Collections.emptyList();
		}
		
		private Station(String named) {
			this.label = named;
			this.items = Collections.emptyList();
		}
		
		public String label() {
			return label;
		}
		
		public List<String> items() {
			return items;
		}
		
		public static Station named(String name) {
			return new Station(name);
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Station)) {
				return false;
			}
			Station other = (Station) o;
			return other.label.equals(label);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(label);
		}
	} 

	public static class Item {
		@Key
		private String id;

		@Key
		private String label;

		@Key
		private String description;

		@Key
		private String station;

		public String getId() {
			return id;
		}

		public String getLabel() {
			return label;
		}

		public String getDescription() {
			return description;
		}

		public Station getStation() {
			return Station.named(station);
		}
	}
}
