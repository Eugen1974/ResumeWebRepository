package com.urise.webapp.storage.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.urise.webapp.model.Resume;

public class JacksonStreamSerializer implements IStreamSerializer {

	private static final ObjectMapper PARSER;

	static {
		PARSER = new ObjectMapper();
		PARSER.configure(SerializationFeature.INDENT_OUTPUT, true); // pretty
																	// print
	}

	@Override
	public void doWrite(Resume resume, OutputStream out) throws IOException {
		PARSER.writeValue(out, resume);
	}

	@Override
	public Resume doRead(InputStream in) throws IOException {
		return PARSER.readValue(in, Resume.class);
	}
}
