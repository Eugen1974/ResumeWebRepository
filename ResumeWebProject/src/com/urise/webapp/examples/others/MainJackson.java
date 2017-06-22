package com.urise.webapp.examples.others;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ITestData;

public class MainJackson {

	public static final ObjectMapper PARSER;

	static {
		PARSER = new ObjectMapper();
		PARSER.configure(SerializationFeature.INDENT_OUTPUT, true); // pretty
																	// print
	}

	public static void main(String[] args) throws IOException {
		String strResume = PARSER.writeValueAsString(ITestData.RESUME_2);
		System.out.println(strResume);

		Resume resume = PARSER.readValue(strResume, Resume.class);
		System.out.println("\n" + resume);
	}
}
