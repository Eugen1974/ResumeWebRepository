package com.urise.webapp.storage.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.urise.webapp.model.Resume;
import com.urise.webapp.util.JsonParser;

public class JsonStreamSerializer implements IStreamSerializer {

	@Override
	public Resume doRead(InputStream in) throws IOException {
		try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			return JsonParser.read(reader, Resume.class);
		}
	}

	@Override
	public void doWrite(Resume resume, OutputStream out) throws IOException {
		try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
			JsonParser.write(resume, writer);
		}
	}
}
