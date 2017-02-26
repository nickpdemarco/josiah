package edu.brown.cs.ndemarco.josiah;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import ai.api.model.AIRequest;
import ai.api.model.Result;
import spark.Request;

public class JosiahAIRequest extends AIRequest {

  /**
   * Auto generated
   */
  private static final long serialVersionUID = 1L;

  @SerializedName("result")
  private Result result;


  private JosiahAIRequest() {} // For now.


  public static JosiahAIRequest fromSparkRequest(final Request request)
      throws JsonSyntaxException {
    return Constants.GSON.fromJson(request.body(), JosiahAIRequest.class);
  }

  public Result result() {
    return result;
  }

  public String toJson() {
    return Constants.GSON.toJson(this);
  }


}
