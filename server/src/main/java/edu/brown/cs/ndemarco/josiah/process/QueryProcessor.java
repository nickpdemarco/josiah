package edu.brown.cs.ndemarco.josiah.process;

import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;

@FunctionalInterface
public interface QueryProcessor {
	JosiahFulfillment process(JosiahQuery query);
}
