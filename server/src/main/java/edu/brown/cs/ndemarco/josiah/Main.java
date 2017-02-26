package edu.brown.cs.ndemarco.josiah;

import com.google.gson.JsonSyntaxException;

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
      try {

        JosiahAIRequest req = JosiahAIRequest.fromSparkRequest(request);
        JosiahAIResponse resp = JosiahAIResponse.simple(
            String.format("You said: %s", req.result().getResolvedQuery()));
        return resp.toJson();

      } catch (JsonSyntaxException e) {
        return JosiahAIResponse.error(
            String.format("Error parsing json: %s", e.getCause()),
            Constants.STATUS_CODE_JOSIAH_ERROR)
            .toJson();
      }
    }


  }


}
