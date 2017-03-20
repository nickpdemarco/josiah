package edu.brown.cs.ndemarco.josiah;

import java.util.Collections;

import ai.api.model.Fulfillment;

/**
 * This class serves to be a simple extension of APIAI's Fulfillment.
 * 
 * @author nickpdemarco
 *
 */
public class JosiahFulfillment extends Fulfillment {

	/**
	 * AUTO GENERATED
	 */
	private static final long serialVersionUID = 1L;

	public static final JosiahFulfillment simple(String format, Object...objects) {
		return simple(String.format(format, objects));
	}
	
	public static final JosiahFulfillment simple(String speech) {
		JosiahFulfillment f = simple();
		f.setSpeech(speech);
		return f;
	}

	public static final JosiahFulfillment simple() {
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
		return simple(message);
	}

}
