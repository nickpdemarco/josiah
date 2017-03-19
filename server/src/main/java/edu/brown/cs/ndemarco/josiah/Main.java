package edu.brown.cs.ndemarco.josiah;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.ndemarco.josiah.process.QueryDelegator;
import edu.brown.cs.ndemarco.josiah.process.QueryProcessor;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public abstract class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		runSparkServer();
	}

	private static void runSparkServer() {
		Spark.post("/echo", new EchoHandler());
		Spark.post("/handle", new RequestHandler());
	}

	private static class EchoHandler implements Route {

		@Override
		public Object handle(Request request, Response response) {
			LOGGER.info(String.format("REQUEST: %s", request.body().replaceAll("\\s", "")));

			response.type(Constants.RESPONSE_HEADER_TYPE);
			return Constants.GSON.toJson(Simple.fulfillment("Hello, world!"));
		}

	}

	private static class RequestHandler implements Route {

		private QueryProcessor qp;
		private JsonParser parser = new JsonParser();
		private Gson gson = new GsonBuilder().setPrettyPrinting().create();

		RequestHandler() {
			qp = new QueryDelegator();
		}

		@Override
		public Object handle(Request request, Response response) {
			JsonObject json = parser.parse(request.body()).getAsJsonObject();
			System.out.println(String.format("Request:\n%s", gson.toJson(json)));

			response.type(Constants.RESPONSE_HEADER_TYPE);
			JosiahQuery userQuery = Constants.GSON.fromJson(request.body(), JosiahQuery.class);
			JosiahFulfillment fulfilled = qp.process(userQuery);
			return Constants.GSON.toJson(fulfilled);

		}
	}
}
