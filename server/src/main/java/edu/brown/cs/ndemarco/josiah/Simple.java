package edu.brown.cs.ndemarco.josiah;

import java.util.Collections;

import ai.api.model.Fulfillment;

public class Simple {


  public static final JosiahFulfillment fulfillment(String speech) {
    JosiahFulfillment f = fulfillment();
    f.setSpeech(speech);
    return f;
  }

  public static final JosiahFulfillment fulfillment() {
    JosiahFulfillment f = new JosiahFulfillment();
    f.setContextOut(Collections.emptyList());
    f.setData(Collections.emptyMap());
    f.setDisplayText("");
    f.setSource("");
    f.setSpeech("");
    return f;
  }
  
  public static final JosiahFulfillment error(String message) {
	  // TODO find a better way to signal errors to API.ai
	  return fulfillment(message);
  }
}
