package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

import edu.brown.cs.ndemarco.brownapi.Error;
import edu.brown.cs.ndemarco.brownapi.Failable;

public class Response implements Failable {
	@Key
	private List<Day> days;
	@Key
	private Map<String, Item> items;
	
	// Failable
	private Error error;

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
		return stations;
	}

	@Override
	public void fail(Error e) {
		this.error = e;
		
	}

	@Override
	public boolean failed() {
		return error != null;
	}

	@Override
	public Error error() {
		return error;
	}
}
