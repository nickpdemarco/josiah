package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.List;
import com.google.api.client.util.Key;


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