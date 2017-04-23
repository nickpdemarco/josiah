package edu.brown.cs.ndemarco.josiah.Office;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.brown.cs.ndemarco.brownapi.office.Authorization;

class AuthorizationGenerator {
	
	private AuthorizationGenerator() {} // Don't construct me.
	
	public static Authorization authorizationFromFilePath(String filePath) {
		// Assumes the file at filePath is a single line in the format
		// %s:%s where the strings are username:password.
		
		try (Scanner in = new Scanner(new File(filePath))) {
			in.useDelimiter(":");
			
			return new Authorization.Builder()
					.withUsername(in.next()) // username is first
					.withPassword(in.next().replace("\n", "")) // password is second.
					.build();
			
		} catch (FileNotFoundException e) {
			System.out.format("ERROR: NO FILE AT %s\n", filePath);
			throw new RuntimeException("Authorization file needed for proper execution");
		}
	}

}
