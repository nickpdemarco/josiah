package edu.brown.cs.ndemarco.brownapi.Dining;

import org.jsoup.Jsoup;

/**
 * A simple utility class for cleaning up the returned values from the cafebonappetit API.
 * Not to be used outside of the Dining package.
 * @author nickpdemarco
 *
 */
class Clean {

	static String removeHtmlTags(String dirty) {
		return Jsoup.parse(dirty).text();
	}
	
	static String removeLeadingAtSign(String dirty) {
		if (dirty.length() > 0 && dirty.charAt(0) == '@') {
			return dirty.substring(1, dirty.length());
		} else {
			return dirty;
		}
	}
}
