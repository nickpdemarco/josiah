package edu.brown.cs.ndemarco.josiah.apiaiUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApiAiDate {
	
	@FunctionalInterface
	private interface Rule {
		Date generate(String string);
	}
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Map<String, Rule> rules = new HashMap<>();
	static {
		rules.put("next week", (s) -> {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 7);
			return cal.getTime();
		});
		rules.put("next month", (s) -> {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			return cal.getTime();
		});
		rules.put("next year", (s) -> {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, 1);
			return cal.getTime();
		});
		
	}
	
	public static Date parse(String string) {
		string = string.toLowerCase();
		if (rules.containsKey(string)) {
			return rules.get(string).generate(string);
		}
		try {
			return DATE_FORMAT.parse(string);
		} catch (ParseException e) {
			System.out.format("ERROR: Parse exception while making date string. I don't know how to parse %s\n", string);
		}
		return new Date(); // Today, now.
	}
	
	
}





