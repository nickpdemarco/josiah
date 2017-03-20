package edu.brown.cs.ndemarco.josiah;

public interface QueryProcessor {
	
	JosiahFulfillment process(JosiahQuery query);
	
	boolean isResponsibleFor(String intent);
}
