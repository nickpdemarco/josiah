package edu.brown.cs.ndemarco.josiah.Office;

import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.QueryProcessor;

public class OfficeProcessor implements QueryProcessor {

	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		throw new UnsupportedOperationException("Not yet");
	}

	@Override
	public boolean isResponsibleFor(String intent) {
		return false;
	}

}
