package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

public class Response {
	@Key
	private List<Day> days;

	@Key
	private Map<String, Item> items;

	public Response() {
		items = Collections.emptyMap();
	}
	
	public Response(List<Day> days, Map<String, Item> items) {
		this.days = days;
		this.items = items;
	}
	
	public List<Day> days() {
		return days;
	}

	public Map<String, Item> items() {
		return items;
	}
	

	public static Response emptyResponse() {
		return new Response();
	}
}
