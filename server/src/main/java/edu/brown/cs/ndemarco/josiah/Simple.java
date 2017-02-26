package edu.brown.cs.ndemarco.josiah;

import java.util.Collections;

import ai.api.model.Fulfillment;

class Simple {


  public static final Fulfillment fulfillment(String speech) {
    Fulfillment f = fulfillment();
    f.setSpeech(speech);
    return f;
  }

  public static final Fulfillment fulfillment() {
    Fulfillment f = new Fulfillment();
    f.setContextOut(Collections.emptyList());
    f.setData(Collections.emptyMap());
    f.setDisplayText("");
    f.setSource("");
    f.setSpeech("");
    return f;
  }
}
