package edu.brown.cs.ndemarco.josiah;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.annotations.SerializedName;

import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Status;

public class JosiahAIResponse extends AIResponse {

  /**
   * auto generated
   */
  private static final long serialVersionUID = 1L;


  @SerializedName("fulfillment")
  private Fulfillment fulfillment;

  private JosiahAIResponse() {} // For now.


  // Note that if the error code given matches something in the
  // Status.class reference map, the given message will overwrite the default
  // from Status.
  public static JosiahAIResponse error(String message, Integer errorCode) {
    checkNotNull(message);
    checkNotNull(errorCode);

    JosiahAIResponse resp = new JosiahAIResponse();
    Status stat = Status.fromResponseCode(errorCode);
    stat.setErrorDetails(message);

    resp.setStatus(stat);
    return resp;
  }

  public static JosiahAIResponse simple(String message) {
    checkNotNull(message);
    JosiahAIResponse resp = new JosiahAIResponse();

    resp.fulfillment = new Fulfillment();
    resp.fulfillment.setSpeech(message);

    return resp;
  }

  public String toJson() {
    return Constants.GSON.toJson(this);
  }

}
