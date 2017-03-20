package edu.brown.cs.ndemarco.brownapi.Dining;

public enum MEAL_TIME {
	BREAKFAST(1),
	BRUNCH(2), // Appears wholly unused by the api (at least for Brown)
	LUNCH(3),
	DINNER(4),
	DESSERT(5), // unclear how the API represents this. TODO figure it out. id of 6 also appears to return desserts. Don't recommend using it.
	// UNKNOWN(6), // This option appears to return unusual results. Sometimes dessert. 
	LATENITE(7); // In the API as "Late Night"
	
	private int id;
	
	MEAL_TIME(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String displayString() {
		return this.toString().toLowerCase();
	}
}
