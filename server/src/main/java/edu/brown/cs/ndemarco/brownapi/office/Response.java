package edu.brown.cs.ndemarco.brownapi.office;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.util.Key;

public class Response {
	
	@Key
	private List<Person> people;
	
	@Key
	private String status;
	
	@Key
	private Data data; 
	
	public static Response emptyResponse() {
		return new Response();
	}
	
	public Response() {
		this.people = new ArrayList<>();
	}
	
	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}
	
	public boolean failed() {
		return status != null;
	}
	
	public String errorMessage() {
		return failed() ? data.getDetailedMessage() : ""; 
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
		public String getOffice_hours() {
			return office_hours;
		}
		public String getFirst_name() {
			return first_name;
		}
		public String getLast_name() {
			return last_name;
		}
		public String getBrown_id() {
			return brown_id;
		}
		public String getTitle() {
			return title;
		}
		public String getLocation() {
			return location;
		}
	}
	
	// Data field for error messages
	private static class Data {
		@Key
		private String detailedMessage;
		
		@Key
		private String exceptionCode;
		
		public String getDetailedMessage() {
			return detailedMessage;
		}
		
		@SuppressWarnings("unused")
		public String getExceptionCode() {
			return exceptionCode;
		}
	}

}
