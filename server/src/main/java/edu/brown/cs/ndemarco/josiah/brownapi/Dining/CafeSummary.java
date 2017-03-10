package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.List;
import com.google.api.client.util.Key;

public class CafeSummary {
	@Key
	private String name;
	
	// A list of dayparts for every day requested.
	@Key
	private List<List<Daypart>> dayparts;
	
	public String name() { return name; }
	public List<List<Daypart>> dayparts () { return dayparts; }
}
