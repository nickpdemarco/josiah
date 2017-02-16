package edu.brown.cs.ndemarco.josiah;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public abstract class Main {

  public static void main(String[] args) {
    runSparkServer();
  }


  private static void runSparkServer() {
    Spark.post("/hello", new HelloHandler());
  }


  private static class HelloHandler implements Route {

    private final Gson gson;
    private final Map<String, String> resp;

    public HelloHandler() {
      gson = new Gson();
      resp = new HashMap<>();
      resp.put("Hello", "world");
    }

    @Override
    public Object handle(Request request, Response response) {
      return gson.toJson(resp);
    }


  }


}
