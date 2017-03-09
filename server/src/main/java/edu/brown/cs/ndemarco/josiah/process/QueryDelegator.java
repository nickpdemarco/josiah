package edu.brown.cs.ndemarco.josiah.process;

import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.Simple;

public class QueryDelegator implements QueryProcessor {

	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		
		// TODO switch on intent-id instead of name!
		switch (query.getResult().getMetadata().getIntentName()) {
		case "menu":
			return new MenuProcessor().process(query);
		default:
			return Simple.error("That's something I don't know how to do.");
		}
	}

}
