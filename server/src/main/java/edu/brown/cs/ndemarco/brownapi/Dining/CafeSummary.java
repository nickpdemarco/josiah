package edu.brown.cs.ndemarco.brownapi.Dining;

import java.util.List;
import com.google.api.client.util.Key;

/**
 * The top-level field in the JSON response from the API.
 * Other fields are provided by cafebonappetit, but are omitted here. These include, but are not limited to:
 * address, city, state, zip, latitude, longitude, description, time_zone.
 * @author nickpdemarco
 *
 */
public class CafeSummary {
	@Key
	private String name;
	
	// A list of dayparts for every day requested.
	@Key
	private List<List<Daypart>> dayparts;
	
	public String name() { return name; }
	public List<List<Daypart>> dayparts () { return dayparts; }
}
