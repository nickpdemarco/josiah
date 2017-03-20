package edu.brown.cs.ndemarco.brownapi.Dining;

public enum DINING_HALL {
	RATTY(1531),
	VDUB(1532),
	ANDREWS(1533),
	BLUEROOM(1534),
	JOS(1535),
	IVYROOM(1536),
	CAMPUS_MARKET(1537),
	CAFE_CARTS(1538);
	
	private int id;
	
	DINING_HALL(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}