package edu.brown.cs.ndemarco.brownapi.Dining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import com.google.api.client.util.Key;


public class Station {
	// MARK: Static:
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
	
	public static Station withId(String id) {
		return new Station(id);
	}
	
	// MARK: Instance Vars
	
	@Key
	private String id;
	
	@Key
	private String label;
	
	@Key("items")
	private List<String> itemIds;
	
	private List<Item> items;
	
	// MARK: Constructors
	
	// Principally used by Jackson
	public Station() {
		this.label = "";
		this.id = "";
		// This field is set by the parser.
		this.itemIds = Collections.emptyList();
		// And this field is manipulated by me.
		this.items = new ArrayList<>();
	}
	
	// We use this for statically generating "placeholder" stations for map keys, etc.
	// Any changes to this should also be reflected in the equals() method.
	private Station(String id) {
		this.label = "";
		this.id = id;
		this.itemIds = Collections.emptyList();
		this.items = Collections.emptyList();
	}
	
	// MARK: Methods
	
	public String id() { return id; }
	public String label() { return label; }
	public List<String> itemIds() { return itemIds; }
	// Package visible - should be immutable outside of the package.
	void label(String label) { this.label = label; }
	
	void addItem(Item item) {
		int currIdx = items.size();
		assert item.id().equals(itemIds.get(currIdx)) : "Added items in the wrong order";
		items.add(item);
	}
	
	// MARK: equals, hashCode, toString
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Station)) {
			return false;
		}
		Station other = (Station) o;
		return other.id.equals(id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override 
	public String toString() {
		if (displayOverrides.containsKey(id)) {
			return displayOverrides.get(id);
		}
		if (items.isEmpty()) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(items.get(0)); // we know this exists due to check above.
		for (int i = 1; i < items.size() - 1; i++) {
			sb.append(String.format(", %s", items.get(i)));
		}
		
		sb.append(
				(items.size() == 1) ? ". " : 
					String.format(", and %s. ", items.get(items.size() - 1)));
		return sb.toString().trim(); // Remove that last trailing space.
	}
} 