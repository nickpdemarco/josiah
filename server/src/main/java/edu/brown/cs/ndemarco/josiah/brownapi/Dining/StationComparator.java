package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Dining.Station;

public class StationComparator implements Comparator<Dining.Station>{
	
	private static final Map<Station, Integer> ranks = new HashMap<>();
	static {
		// We first sort by eatery, then rank the most-likely-wanted station.
		// RATTY:
		ranks.put(Dining.Station.named("comforts"), 1);
		ranks.put(Dining.Station.named("roots and shoots"), 2);
		ranks.put(Dining.Station.named("sweets"), 4);
		ranks.put(Dining.Station.named("kettles"), 3);
		ranks.put(Dining.Station.named("omelet bar"), 5);
		
		ranks.put(Dining.Station.named("salad bar"), 10);
	}

	@Override
	public int compare(Station o1, Station o2) {
		int o1Rank = ranks.containsKey(o1) ? ranks.get(o1) : Integer.MAX_VALUE;
		int o2Rank = ranks.containsKey(o2) ? ranks.get(o2) : Integer.MAX_VALUE;
		return o1Rank - o2Rank;		
	}

}
