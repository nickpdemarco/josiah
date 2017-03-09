package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.Map;
import com.google.api.client.util.Key;

public  class Day {
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
