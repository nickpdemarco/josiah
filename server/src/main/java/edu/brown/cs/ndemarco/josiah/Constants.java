
package edu.brown.cs.ndemarco.josiah;

import com.google.gson.Gson;

import ai.api.GsonFactory;

public class Constants {

  public static final int STATUS_CODE_JOSIAH_ERROR = 100;
  public static final int STATUS_CODE_SUCCESS = 200;

  public static final Gson GSON = GsonFactory.getDefaultFactory().getGson();

}
