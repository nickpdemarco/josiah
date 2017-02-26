package edu.brown.cs.ndemarco.josiah;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  }


  private static class EchoHandler implements Route {

    public EchoHandler() {}


    @Override
    public Object handle(Request request, Response response) {
      LOGGER.info(String.format("REQUEST: %s", request.body().replaceAll("\\s", "")));

      response.type("application/json");
      return Constants.GSON.toJson(Simple.fulfillment("Hello, world!"));
    }


  }


}
