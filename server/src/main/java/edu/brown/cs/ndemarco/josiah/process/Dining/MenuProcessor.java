package edu.brown.cs.ndemarco.josiah.process.Dining;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.QueryProcessor;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.DINING_HALL;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Dining;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.MEAL_TIME;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Response;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Station;

public class MenuProcessor implements QueryProcessor {

	// TODO statically initialize these at compile time by checking a directory
	private static final Map<String, DINING_HALL> halls = new HashMap<>();
	static {
		halls.put("Sharpe Refactory", DINING_HALL.RATTY);
		halls.put("Verney Wooley", DINING_HALL.VDUB);
		halls.put("Andrews Dining Hall", DINING_HALL.ANDREWS);
		halls.put("Blue Room", DINING_HALL.BLUEROOM);
		halls.put("Josiah's", DINING_HALL.JOS);
		halls.put("Ivy Room", DINING_HALL.IVYROOM);
		halls.put("Campus Market", DINING_HALL.CAMPUS_MARKET);
	}

	private static final Map<String, MEAL_TIME> meals = new HashMap<>();
	static {
		meals.put("Breakfast", MEAL_TIME.BREAKFAST);
		meals.put("Lunch", MEAL_TIME.LUNCH);
		meals.put("Dinner", MEAL_TIME.DINNER);
	}

	static {
		// See comments in Station.java
		// Be sure to end with periods for good formatting!

		// Blue Room
		Station.assignOverride("12422", "Custom sandwiches.");
		Station.assignOverride("12433", "Frittata breakfast sandwiches.");
		Station.assignOverride("12421", "Focaccia sandwiches.");
		Station.assignOverride("12174", "Fresh salad bar.");

	}

	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		String diningHall = query.getResult().getStringParameter("building");
		String meal = query.getResult().getStringParameter("meal");
		String date = query.getResult().getStringParameter("date");

		Dining.QueryBuilder dqb = new Dining.QueryBuilder();

		if (halls.containsKey(diningHall)) {
			dqb.withDiningHall(halls.get(diningHall));
		} else {
			return JosiahFulfillment.simple("It looks like %s doesn't sell food.", diningHall);
		}

		if (meals.containsKey(meal)) {
			dqb.withMealTime(meals.get(meal));
		}

		dqb.withDate(date);
		Response response = dqb.execute();

		if (response.failed()) {
			Exception e = response.error().exception();
			String userFriendlyReason = response.error().userFriendlyReason();
			System.out.format("ERROR: %s :: %s", (e == null) ? "No exception" : e.getMessage(), userFriendlyReason);

			return JosiahFulfillment.simple(userFriendlyReason);
		}

		return JosiahFulfillment.simple(speechResponse(response));
	}

	private String speechResponse(Response response) {
		List<Station> stations = new ArrayList<>(response.stations());
		stations.sort(new StationComparator());
		StringBuilder sb = new StringBuilder();
		for (Station s : stations) {
			sb.append(String.format("%s ", s.toString()));
		}
		return sb.toString().trim(); // remove the last trailing whitespace.
	}

	@Override
	public boolean isResponsibleFor(String intent) {
		return intent.equals("menu");
	}

}
