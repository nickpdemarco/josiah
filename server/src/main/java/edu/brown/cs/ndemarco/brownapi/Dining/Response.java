package edu.brown.cs.ndemarco.brownapi.Dining;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

public class Response {
	@Key
	private List<Day> days;
	@Key
	private Map<String, Item> items;
	
	private List<Station> stations;
	
	public static Response emptyResponse() { return new Response(); }
	
	public Response() {
		items = Collections.emptyMap();
	}
	
	public Response(List<Day> days, Map<String, Item> items) {
		this.days = days;
		this.items = items;
	}
	
	public List<Day> days() { return days; }
	public Map<String, Item> items() { return items; }
	
	public List<Station> stations(){
		if (stations != null) {
			List<Station> stations = new ArrayList<>();
			// The hyper-nested loop! Don't be scared - because this is only
			// iterating over the deserialized json object, it may look gross,
			// but is actually fairly quick. 
			// Most of these fields have one element in them for basic queries.
			for (Day day : days) {
				for (CafeSummary cafesummary : day.cafes().values()) {
					for (List<Daypart> daypartList : cafesummary.dayparts()) {
						for (Daypart daypart : daypartList) {
							for (Station station : daypart.stations()) {
								stations.add(station);
							}
						}
					}
				}
			}
		}
		return stations;
	}
}
