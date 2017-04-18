package edu.brown.cs.ndemarco.brownapi.office;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ResponseAdapter extends TypeAdapter<Response> {
	
	private static final Gson GSON = new Gson();

	@Override
	public void write(JsonWriter out, Response value) throws IOException {
		throw new UnsupportedOperationException("We don't support writing Responses yet.");
	}

	@Override
	public Response read(JsonReader in) throws IOException {
		Response response = new Response();
		Response.Person[] people = GSON.fromJson(in, Response.Person[].class);
		response.setPeople(people);
		return response;
	}

}
