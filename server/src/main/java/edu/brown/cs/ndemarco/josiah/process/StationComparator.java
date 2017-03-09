package edu.brown.cs.ndemarco.josiah.process;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Station;

public class StationComparator implements Comparator<Station>{
	
	private static final Map<Station, Integer> ranks = new HashMap<>();
	private static int rank = 1;
	static {
		// We first sort by eatery, then rank the most-likely-wanted station.
		// This works only if we limit queries to one eatery at a time.
		// RATTY:
		ranks.put(Station.withId("12176"), rank++); // comforts
		ranks.put(Station.withId("12177"), rank++); // roots and shoots
		ranks.put(Station.withId("12175"), rank++); // kettles
		ranks.put(Station.withId("12278"), rank++); // sweets
		ranks.put(Station.withId("13506"), rank++); // omelet bar
		ranks.put(Station.withId("12179"), rank++); // pasta
		ranks.put(Station.withId("12174"), rank++); // salad bar
		ranks.put(Station.withId("12305"), rank++); // pizza
		ranks.put(Station.withId("12178"), rank++); // from the grill
		ranks.put(Station.withId("12174"), rank++); // deli
		ranks.put(Station.withId("12315"), rank++); // roots & shoots bar
		ranks.put(Station.withId("12520"), rank++); // breakfast for lunch
		
		// V DUB
		ranks.put(Station.withId("12368"), rank++); // hot breakfast bar
		ranks.put(Station.withId("12418"), rank++); // grill
		ranks.put(Station.withId("12420"), rank++); // stir fry station
		ranks.put(Station.withId("12404"), rank++); // soup and stew
		ranks.put(Station.withId("12369"), rank++); // specialty bar
		ranks.put(Station.withId("12419"), rank++); // comfort food bar
		ranks.put(Station.withId("12366"), rank++); // waffle station
		ranks.put(Station.withId("12367"), rank++); // omlet station
		ranks.put(Station.withId("12405"), rank++); // dessert table
		ranks.put(Station.withId("12402"), rank++); // salad bar
		ranks.put(Station.withId("12403"), rank++); // deli
		ranks.put(Station.withId("12405"), rank++); // dessert table
		
		
		// Andrews
		ranks.put(Station.withId("12342"), rank++); // special
		ranks.put(Station.withId("12343"), rank++); // pho
		ranks.put(Station.withId("12341"), rank++); // hearth
		ranks.put(Station.withId("12494"), rank++); // Late Nigh Grab & Go (typo in API?)
		ranks.put(Station.withId("12344"), rank++); // wok
		ranks.put(Station.withId("12346"), rank++); // panini
		ranks.put(Station.withId("12487"), rank++); // dessert

		// Blue Room
		ranks.put(Station.withId("12424"), rank++); // kabob & curry
		ranks.put(Station.withId("12422"), rank++); // deli sandwich
		ranks.put(Station.withId("12347"), rank++); // bakery
		ranks.put(Station.withId("12433"), rank++); // frittata & bfast sandwich bar
		ranks.put(Station.withId("12348"), rank++); // soup
		ranks.put(Station.withId("12421"), rank++); // focaccia bar
		
		
		// Josiah's
		ranks.put(Station.withId("12031"), rank++); // Panini Grill Sun/Mon/wed Closes at 11pm
		ranks.put(Station.withId("12032"), rank++); // Quesadillas Tue/Thursday/Fri/Sat
		ranks.put(Station.withId("12028"), rank++); // Three burners Monday through Friday
		ranks.put(Station.withId("12365"), rank++); // Three burners Weekends
		ranks.put(Station.withId("12029"), rank++); // Cutting board
		ranks.put(Station.withId("12030"), rank++); // Grill
		
	
		// Ivy Room
		ranks.put(Station.withId("12447"), rank++); // Dinner Entrees
		ranks.put(Station.withId("12361"), rank++); // entrées & sides
		ranks.put(Station.withId("12429"), rank++); // grill
		ranks.put(Station.withId("12427"), rank++); // signature sandwiches
		ranks.put(Station.withId("12360"), rank++); // soup
		ranks.put(Station.withId("12449"), rank++); // Falafel Sandwich bar
		ranks.put(Station.withId("12425"), rank++); // falafel
		ranks.put(Station.withId("12428"), rank++); // build-your-own sandwich		
		ranks.put(Station.withId("12444"), rank++); // Smoothies
		ranks.put(Station.withId("12445"), rank++); // Tacos
		ranks.put(Station.withId("12446"), rank++); // Bruschetta Bar
		ranks.put(Station.withId("12448"), rank++); // Pizza
		ranks.put(Station.withId("12426"), rank++); // tossed salad
		ranks.put(Station.withId("12430"), rank++); // bread
		ranks.put(Station.withId("12362"), rank++); // dessert
		
		// TODO? not sorting Café carts or Campus market. Not sure what a good ordering would even be there.
		
		// Make sure everything is unique
		Set<String> seenAlready = new HashSet<>();
		for (Station key : ranks.keySet()) {
			assert !seenAlready.contains(key.id()) : "Error: Station Id's are not unique.";
			seenAlready.add(key.id());
		}		
	}

	@Override
	public int compare(Station o1, Station o2) {
		int o1Rank = ranks.containsKey(o1) ? ranks.get(o1) : Integer.MAX_VALUE;
		int o2Rank = ranks.containsKey(o2) ? ranks.get(o2) : Integer.MAX_VALUE;
		return o1Rank - o2Rank;		
	}

}
