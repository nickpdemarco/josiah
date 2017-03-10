package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import org.jsoup.Jsoup;

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
