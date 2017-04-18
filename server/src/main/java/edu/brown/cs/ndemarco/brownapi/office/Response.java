package edu.brown.cs.ndemarco.brownapi.office;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.util.Key;

public class Response {
	
	private List<Person> people;
	 
	public static Response emptyResponse() {
		return new Response();
	}
	
	public Response() {
		this.people = new ArrayList<>();
	}
		
	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}
	
	// package visible only! Responses should be immutable.
	void setPeople(List<Person> people) {
		this.people = people;
	}
	
	// package visible only! Responses should be immutable.
	void setPeople(Person[] people) {
		this.people = Arrays.asList(people);
	}
	
	
	public static class Person {
		@Key
		private String department;
		@Key
		private String office_hours;
		@Key
		private String first_name;
		@Key
		private String last_name;
		@Key
		private String brown_id;
		@Key
		private String title;
		@Key
		private String location;
		
		public String getDepartment() {
			return department;
		}
		public String getOfficeHours() {
			return office_hours;
		}
		public String getFirstName() {
			return first_name;
		}
		public String getLastName() {
			return last_name;
		}
		public String getBrownId() {
			return brown_id;
		}
		public String getTitle() {
			return title;
		}
		public String getLocation() {
			return location;
		}
	}
	
}
