package edu.brown.cs.ndemarco.josiah;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.api.model.AIResponse;
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
	  
	  RequestHandler() {
		  qp = new QueryDelegator();
	  }
	  
	  @Override
	  public Object handle(Request request, Response response) {
		  response.type(Constants.RESPONSE_HEADER_TYPE);
		  JosiahQuery userQuery = Constants.GSON.fromJson(request.body(), JosiahQuery.class);
		  JosiahFulfillment fulfilled = qp.process(userQuery);
		  return Constants.GSON.toJson(fulfilled);
		
	  }
  }


}
