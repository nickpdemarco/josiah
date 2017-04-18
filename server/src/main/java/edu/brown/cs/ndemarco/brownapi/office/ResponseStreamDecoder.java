package edu.brown.cs.ndemarco.brownapi.office;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ResponseStreamDecoder {
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Response.class, new ResponseAdapter())
			.create();
	
	private ResponseStreamDecoder() {}
	
	public static Response decode(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(response.getContent()));

		StringBuilder content = new StringBuilder();
		String line;
		while (null != (line = br.readLine())) {
		    content.append(line);
		}
		
		return GSON.fromJson(content.toString(), Response.class);
		
	}

}
