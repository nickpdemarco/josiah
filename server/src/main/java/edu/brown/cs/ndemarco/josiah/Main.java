package edu.brown.cs.ndemarco.josiah;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;


public abstract class Main {

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

      response.type("application/json");
      System.out.println(request.headers());
      System.out.println("-----=====-----");
      System.out.println(request.body());

      return Constants.GSON.toJson(Simple.fulfillment("Hello, world!"));
    }


  }


}
