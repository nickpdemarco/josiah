package edu.brown.cs.ndemarco.brownapi.Dining;

public enum DININGHALL {
	RATTY(1531),
	VDUB(1532),
	ANDREWS(1533),
	BLUEROOM(1534),
	JOS(1535),
	IVYROOM(1536),
	CAMPUS_MARKET(1537),
	CAFE_CARTS(1538);
	
	private int id;
	
	DININGHALL(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}