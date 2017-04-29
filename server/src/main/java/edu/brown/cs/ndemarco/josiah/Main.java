package edu.brown.cs.ndemarco.josiah;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.ndemarco.josiah.Dining.MenuProcessor;
import edu.brown.cs.ndemarco.josiah.Office.OfficeProcessor;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public abstract class Main {

	public static void main(String[] args) {
		runSparkServer();
	}

	private static void runSparkServer() {
		Spark.setPort(4567);
		Spark.get("/", new RootHandler());
		Spark.post("/handle", new RequestHandler());
	}
	
	private static class RootHandler implements Route {

		@Override
		public Object handle(Request request, Response response) {
			return "You've reached the home page for the Josiah project.\n" +
					"We're still in development. Contact nicholas_demarco@brown.edu for details.";
		}
	}
	
	private static class RequestHandler implements Route {

		private Josiah josiah;
		private JsonParser parser = new JsonParser();
		private Gson gson = new GsonBuilder().setPrettyPrinting().create();

		RequestHandler() {
			josiah = new Josiah.Builder().withDefaultProcessors(
					new MenuProcessor(),
					new OfficeProcessor())
					.build();
		}

		@Override
		public Object handle(Request request, Response response) {
			JsonObject json = parser.parse(request.body()).getAsJsonObject();
			System.out.println(String.format("Request:\n%s", gson.toJson(json)));

			response.type(Constants.RESPONSE_HEADER_TYPE);
			JosiahQuery userQuery = Constants.GSON.fromJson(request.body(), JosiahQuery.class);
			JosiahFulfillment fulfilled = josiah.process(userQuery);
			return Constants.GSON.toJson(fulfilled);

		}
	}
}
