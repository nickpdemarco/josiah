package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import com.google.api.client.util.Key;

public class Item {
	@Key
	private String id;

	@Key
	private String label;

	@Key
	private String description;

	// This field should be manually set by the executor of the query!
	private Station station;

	public String id() {
		return id;
	}

	public String label() {
		return label;
	}

	public String description() {
		return description;
	}
	
	public Station station() {
		return station;
	}
	
	void station(Station station) {
		this.station = station;
	}
}