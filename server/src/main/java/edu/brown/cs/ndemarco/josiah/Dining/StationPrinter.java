package edu.brown.cs.ndemarco.josiah.Dining;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.ndemarco.brownapi.Dining.Station;

public class StationPrinter {
	/*
	 The cafebonappetit API returns some unsatisfactory responses in terms
	 of natural language. A great example is the Blue Room's sandwich bar.
	 Understandably, the API returns a list of what breads, meats, etc, are available.
	 But this is an unreasonable thing to actually _say_ to a user. So instead
	 we'd say "custom sandwiches." Users can provide overrides below. 
	 If an ID maps to a string in this map, then toString will return this 
	 display _instead of_ a list of this station's items.
	*/
	private static Map<String, String> displayOverrides = new HashMap<>();
	
	public static void assignOverride(String stationId, String override) {
		displayOverrides.put(stationId, override);
	}
	
	public static String print(Station s) {
		if (displayOverrides.containsKey(s.id())) {
			return displayOverrides.get(s.id());
		}
		return s.toString();
	}
}
