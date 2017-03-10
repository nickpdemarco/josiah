package edu.brown.cs.ndemarco.josiah.brownapi;

public interface Failable {
	
	void fail(Error e);
	
	boolean failed();
	
	Error error();

}
