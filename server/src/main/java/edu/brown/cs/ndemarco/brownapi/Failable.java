package edu.brown.cs.ndemarco.brownapi;

public interface Failable {
	
	void fail(Error e);
	
	boolean failed();
	
	Error error();

}
