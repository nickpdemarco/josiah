package edu.brown.cs.ndemarco.josiah;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.ndemarco.josiah.Dining.MenuProcessor;
import edu.brown.cs.ndemarco.josiah.Office.OfficeProcessor;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public final class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	private Main() {}
	
	private void run() {
		runSparkServer(createEngine());
	}
	
	private FreeMarkerEngine createEngine() {
		Configuration config = new Configuration();
		File templates = new File("src/main/resources/spark/template/freemarker");
		try {
			config.setDirectoryForTemplateLoading(templates);
		} catch (IOException ioe) {
			System.out.printf("ERROR: Unable to use %s for template loading.%n", templates);
			return null;
		}
		config.setOutputEncoding("utf8");
		return new FreeMarkerEngine(config);
	}

	private void runSparkServer(FreeMarkerEngine freemarker) {
		Spark.setPort(4567);
		Spark.externalStaticFileLocation("src/main/resources/static");
		Spark.get("/", new RootHandler(), freemarker);
		Spark.post("/handle", new RequestHandler());
	}



	private class RootHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			Map<String, Object> variables = ImmutableMap
					.of("title", "Josiah");
			return new ModelAndView(variables, "root.ftl");
		}
	}

	private static class RequestHandler implements Route {

		private Josiah josiah;
		private JsonParser parser = new JsonParser();
		private Gson gson = new GsonBuilder().setPrettyPrinting().create();

		RequestHandler() {
			josiah = new Josiah.Builder().withDefaultProcessors(new MenuProcessor(), new OfficeProcessor()).build();
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
