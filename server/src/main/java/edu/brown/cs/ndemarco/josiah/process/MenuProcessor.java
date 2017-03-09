package edu.brown.cs.ndemarco.josiah.process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.Simple;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.DINING_HALL;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Dining;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.MEAL_TIME;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.StationComparator;
import edu.brown.cs.ndemarco.josiah.brownapi.Dining.Dining.Item;

public class MenuProcessor implements QueryProcessor {

	// TODO statically initialize these at compile time by checking a directory
	private static final Map<String, DINING_HALL> halls = new HashMap<>();
	{
		halls.put("Sharpe Refactory", DINING_HALL.RATTY);
		halls.put("Verney Wooley", DINING_HALL.VDUB);
		halls.put("Andrews Dining Hall", DINING_HALL.ANDREWS);
		halls.put("Blue Room", DINING_HALL.BLUEROOM);
		halls.put("Josiah's", DINING_HALL.JOS);
		halls.put("Ivy Room", DINING_HALL.IVYROOM);
		halls.put("Campus Market", DINING_HALL.CAMPUS_MARKET);
	}

	private static final Map<String, MEAL_TIME> meals = new HashMap<>();
	{
		meals.put("Breakfast", MEAL_TIME.BREAKFAST);
		meals.put("Lunch", MEAL_TIME.LUNCH);
		meals.put("Dinner", MEAL_TIME.DINNER);
	}

	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		String diningHall = query.getResult().getStringParameter("building");
		String meal = query.getResult().getStringParameter("meal");

		Dining.QueryBuilder dqb = new Dining.QueryBuilder();

		if (halls.containsKey(diningHall)) {
			dqb.withDiningHall(halls.get(diningHall));
		} else {
			return Simple.fulfillment(String.format("It looks like %s doesn't sell food.", diningHall));
		}

		if (meals.containsKey(meal)) {
			dqb.withMealTime(meals.get(meal));
		}

		Dining.Response response = dqb.execute();
		return Simple.fulfillment(speechResponse(response));
	}
	
	private String speechResponse(Dining.Response response) {
		List<Dining.Item> items = new ArrayList<>(response.items().values());
		items.sort(new ItemSorterByStation());
		StringBuilder sb = new StringBuilder();
		
		for (List<String> values : itemsForStation(items)) {
			if (values.isEmpty()) continue;
			
			sb.append(values.get(0));
			for (int i = 1; i < values.size() - 1; i++) {
				sb.append(String.format(", %s", values.get(i)));
			}
			
			sb.append(
					(values.size() == 1) ? ". " :
						String.format(", and %s. ", values.get(values.size() - 1))
					);
		}
		return sb.toString().trim(); // Remove that last trailing space.
	}
	private List<List<String>> itemsForStation(List<Dining.Item> items) {
		List<List<String>> itemsForStation = new ArrayList<>();
		if (items.isEmpty()) {
			return itemsForStation;
		}
		
		Dining.Item lastItem = items.get(0);
		itemsForStation.add(new ArrayList<>());
		itemsForStation.get(0).add(lastItem.getLabel());
		
		for (int i = 1; i < items.size(); i++) {
			Dining.Item currItem = items.get(i);
			if (!currItem.getStation().equals(lastItem.getStation())) {
				itemsForStation.add(new ArrayList<>());
				lastItem = currItem;
			}
			itemsForStation.get(itemsForStation.size() - 1).add(currItem.getLabel());
		}
		System.out.println(itemsForStation.size());
		return itemsForStation;
	}
	
	private class ItemSorterByStation implements Comparator<Dining.Item> {

		StationComparator sc = new StationComparator();
		
		@Override
		public int compare(Item o1, Item o2) {
			return sc.compare(o1.getStation(), o2.getStation());
		}	
	}	
}
