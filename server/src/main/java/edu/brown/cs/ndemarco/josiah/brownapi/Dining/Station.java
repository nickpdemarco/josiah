package edu.brown.cs.ndemarco.josiah.brownapi.Dining;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


import com.google.api.client.util.Key;


public class Station {
	@Key
	private String id;
	
	@Key
	private String label;
	
	@Key
	private List<String> items;
	
	// Principally used by Json parser.
	public Station() {
		this.label = "";
		this.id = "";
		this.items = Collections.emptyList();
	}
	
	// We use this for statically generating "placeholder" stations for map keys, etc.
	// Any changes to this should also be reflected in the equals() method.
	private Station(String id) {
		this.label = "";
		this.id = id;
		this.items = Collections.emptyList();
	}
	
	public String id() {
		return id;
	}
	
	public String label() {
		return label;
	}
	
	public List<String> items() {
		return items;
	}
	
	// Package private - should be immutable outside of the package.
	void label(String label) {
		this.label = label;
	}
	
	public static Station withId(String id) {
		return new Station(id);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Station)) {
			return false;
		}
		Station other = (Station) o;
		return other.id.equals(id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
} 