package edu.brown.cs.ndemarco.josiah;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

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

	private static final String SSL_PASSWORD_PATH = "../secure/keyStorePass";
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		runSparkServer();
	}

	private static void runSparkServer() {
		
		String pass = getSslPassword();
		Spark.setSecure("../secure/ssl/KeyStore.jks", pass, "../secure/ssl/TrustStore.ts", pass);
		Spark.setPort(80); // TODO need to actually use nginx 
		
		Spark.get("/test", new TestHandler());
		Spark.post("/echo", new EchoHandler());
		Spark.post("/handle", new RequestHandler());
	}
	
	private static String getSslPassword() {
		try (Scanner in = new Scanner(new File(SSL_PASSWORD_PATH))) {
			return in.next();			
		} catch (FileNotFoundException e) {
			System.out.format("ERROR: NO FILE AT %s\n", SSL_PASSWORD_PATH);
			throw new RuntimeException("Authorization file needed for proper execution");
		}
	}

	private static class EchoHandler implements Route {

		@Override
		public Object handle(Request request, Response response) {
			LOGGER.info(String.format("REQUEST: %s", request.body().replaceAll("\\s", "")));

			response.type(Constants.RESPONSE_HEADER_TYPE);
			return Constants.GSON.toJson(JosiahFulfillment.simple("Hello, world!"));
		}

	}
	
	private static class TestHandler implements Route {

		@Override
		public Object handle(Request request, Response response) {
			System.out.println("Test reached");
			return "Hello, world!";
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
