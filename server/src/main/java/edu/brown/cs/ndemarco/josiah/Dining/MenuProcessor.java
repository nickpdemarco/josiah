package edu.brown.cs.ndemarco.josiah.Dining;

import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.ndemarco.brownapi.Dining.DININGHALL;
import edu.brown.cs.ndemarco.brownapi.Dining.Request;
import edu.brown.cs.ndemarco.brownapi.Dining.MEALTIME;
import edu.brown.cs.ndemarco.brownapi.Dining.Response;
import edu.brown.cs.ndemarco.brownapi.Dining.Station;
import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.QueryProcessor;
import edu.brown.cs.ndemarco.josiah.UserFriendly;
import edu.brown.cs.ndemarco.josiah.apiaiUtil.ApiAiDate;

public class MenuProcessor implements QueryProcessor {

	private static final Map<String, DININGHALL> halls = new HashMap<>();
	static {
		halls.put("Sharpe Refactory", DININGHALL.RATTY);
		halls.put("Verney Wooley", DININGHALL.VDUB);
		halls.put("Andrews Dining Hall", DININGHALL.ANDREWS);
		halls.put("Blue Room", DININGHALL.BLUEROOM);
		halls.put("Josiah's", DININGHALL.JOS);
		halls.put("Ivy Room", DININGHALL.IVYROOM);
		halls.put("Campus Market", DININGHALL.CAMPUS_MARKET);
	}

	private static final Map<String, MEALTIME> meals = new HashMap<>();
	static {
		meals.put("Breakfast", MEALTIME.BREAKFAST);
		meals.put("Lunch", MEALTIME.LUNCH);
		meals.put("Dinner", MEALTIME.DINNER);
	}

	static {
		// See comments in Station.java
		// Be sure to end with periods for good formatting!

		// Blue Room
		StationPrinter.assignOverride("12422", "Custom sandwiches.");
		StationPrinter.assignOverride("12433", "Frittata breakfast sandwiches.");
		StationPrinter.assignOverride("12421", "Focaccia sandwiches.");
		StationPrinter.assignOverride("12174", "Fresh salad bar.");

	}

	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		String diningHall = query.getResult().getStringParameter("building");
		String meal = query.getResult().getStringParameter("meal");
		String date = query.getResult().getStringParameter("date");

		Request.Builder diningQueryBuilder = new Request.Builder();

		if (halls.containsKey(diningHall)) {
			diningQueryBuilder.withDiningHall(halls.get(diningHall));
		} else {
			return JosiahFulfillment.simple("It looks like %s doesn't sell food.", diningHall);
		}

		if (meals.containsKey(meal)) {
			diningQueryBuilder.withMealTime(meals.get(meal));
		}

		diningQueryBuilder.withDate(ApiAiDate.parse(date));
		Response response;
		
		try {
			response = diningQueryBuilder.execute();
		} catch (IOException e) {
			return JosiahFulfillment.simple(UserFriendly.OTHER_SERVICE_FAILED);
		}
		
		return JosiahFulfillment.simple(speechResponse(response));
		
	}

	private String speechResponse(Response response) {
		List<Station> stations = new ArrayList<>(response.stations());
		
		if (stations.isEmpty()) {
			return UserFriendly.EMPTY_DINING_RESPONSE;
		}
		
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
