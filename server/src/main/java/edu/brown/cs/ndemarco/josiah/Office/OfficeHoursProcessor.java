package edu.brown.cs.ndemarco.josiah.Office;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import edu.brown.cs.ndemarco.brownapi.office.Request;
import edu.brown.cs.ndemarco.brownapi.UserFriendlyException;
import edu.brown.cs.ndemarco.brownapi.office.Authorization;
import edu.brown.cs.ndemarco.brownapi.office.Response;
import edu.brown.cs.ndemarco.brownapi.office.Response.Person;
import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.QueryProcessor;
import edu.brown.cs.ndemarco.josiah.UserFriendly;

public class OfficeHoursProcessor implements QueryProcessor {
	
	private static Authorization auth = AuthGenerator.authorizationFromFilePath("../secure/officeApiCredentials");
	
	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		// We assume that we've gotten a professor parameter, and it's set as the brown ID as required
		// by the faculty api. 
		String professorId = query.getResult().getStringParameter("professor");
		Response resp;
		try {
			resp = new Request.Builder()
					.withAuthorization(auth)
					.withBrownId(professorId)
					.execute();
		} catch (UserFriendlyException e) {
			return JosiahFulfillment.simple(e.userFriendlyDescription());
		}
		
		List<Person> people = resp.getPeople();
		
		if (people.isEmpty()) {
			return JosiahFulfillment.simple("Unfortunately, I don't have data on that professor.");
		}
		
		if (people.size() > 1) {
			// TODO error handling and logging.
			throw new RuntimeException("UserId's in Brown database are not unique!");
		}
		
		Person prof = resp.getPeople().get(0);
		
		if (prof.getOfficeHours() == null || prof.getOfficeHours() == "") {
			return JosiahFulfillment.simple("It seems like %s %s has not set their office hours in our database.", 
					prof.getFirstName(), 
					prof.getLastName());
		}
		
		return JosiahFulfillment.simple("%s %s has listed their office hours as: %s", 
				prof.getFirstName(), 
				prof.getLastName(), 
				prof.getOfficeHours());
	}

	@Override
	public boolean isResponsibleFor(String intent) {
		return intent.equals("professor_office_hours");
	}
	
	private static class AuthGenerator {
		
		private AuthGenerator() {} // Don't construct me.
		
		public static Authorization authorizationFromFilePath(String filePath) {
			// Assumes the file at filePAth is a single line in the format
			// %s:%s where the strings are username:password.
			
			try (Scanner in = new Scanner(new File(filePath))) {
				in.useDelimiter(":");
				
				return new Authorization.Builder()
						.withUsername(in.next()) // username is first
						.withPassword(in.next().replace("\n", "")) // password is second.
						.build();
			} catch (FileNotFoundException e) {
				System.out.format("ERROR: NO FILE AT %s\n", filePath);
				throw new RuntimeException("Authorization file needed for proper execution");
			}
			
			
			
			

		}
	}

}
