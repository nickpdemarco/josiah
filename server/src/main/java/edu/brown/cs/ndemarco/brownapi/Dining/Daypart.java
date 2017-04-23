package edu.brown.cs.ndemarco.brownapi.Dining;

import java.util.List;
import com.google.api.client.util.Key;

/**
 * An abstraction for a meal time, with possible values such as "Breakfast," "Brunch", "Dinner", etc. 
 * @author nickpdemarco
 *
 */
public class Daypart {
	@Key
	private String label;
	
	@Key
	private String starttime;
	
	@Key
	private String endtime;
	
	@Key
	private List<Station> stations;
	
	public String label() { return label; }
	public String startTime() { return starttime; }
	public String endTime() { return endtime; }
	public List<Station> stations() { return stations; }
}