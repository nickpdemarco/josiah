package edu.brown.cs.ndemarco.josiah.Office;

import java.util.List;

import edu.brown.cs.ndemarco.brownapi.office.Request;
import edu.brown.cs.ndemarco.brownapi.UserFriendlyException;
import edu.brown.cs.ndemarco.brownapi.office.Authorization;
import edu.brown.cs.ndemarco.brownapi.office.Response;
import edu.brown.cs.ndemarco.brownapi.office.Response.Person;
import edu.brown.cs.ndemarco.josiah.JosiahFulfillment;
import edu.brown.cs.ndemarco.josiah.JosiahQuery;
import edu.brown.cs.ndemarco.josiah.QueryProcessor;

public class OfficeProcessor implements QueryProcessor {

	private static final String HOURS_INTENT = "professor_office_hours";
	private static final String LOCATION_INTENT = "professor_office_location";

	private static Authorization auth = AuthorizationGenerator
			.authorizationFromFilePath("../secure/officeApiCredentials");

	@Override
	public JosiahFulfillment process(JosiahQuery query) {
		// We assume that we've gotten a professor parameter, and it's set as
		// the brown ID as required
		// by the faculty api.
		String professorId = query.getResult().getStringParameter("professor");
		Response resp;
		try {
			resp = new Request.Builder().withAuthorization(auth).withBrownId(professorId).execute();
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

		switch (query.getResult().getMetadata().getIntentName()) {
		case HOURS_INTENT:
			return handleOfficeHours(resp.getPeople().get(0));
		case LOCATION_INTENT:
			return handleOfficeLocation(resp.getPeople().get(0));
		default:
			throw new RuntimeException("This should never happen.");
		}
	}

	private JosiahFulfillment handleOfficeHours(Person prof) {
		if (prof.getOfficeHours() == null || prof.getOfficeHours() == "") {
			return JosiahFulfillment.simple("It seems like %s %s has not set their office hours in our database.",
					prof.getFirstName(), prof.getLastName());
		}

		return JosiahFulfillment.simple("%s %s has listed their office hours as: %s", prof.getFirstName(),
				prof.getLastName(), prof.getOfficeHours());
	}

	private JosiahFulfillment handleOfficeLocation(Person prof) {
		if (prof.getLocation() == null || prof.getLocation() == "") {
			return JosiahFulfillment.simple("It seems like %s %s has not set their office location in our database.",
					prof.getFirstName(), prof.getLastName());
		}
		return JosiahFulfillment.simple("%s %s has listed their office location as: %s", prof.getFirstName(),
				prof.getLastName(), prof.getLocation());
	}

	@Override
	public boolean isResponsibleFor(String intent) {
		return intent.equals(HOURS_INTENT) || intent.equals(LOCATION_INTENT);
	}

}
