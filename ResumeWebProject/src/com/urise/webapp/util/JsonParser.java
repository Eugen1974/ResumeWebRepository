package com.urise.webapp.util;

import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urise.webapp.model.Section;

public class JsonParser {

	private final static Gson PARSER = new GsonBuilder()
			.registerTypeAdapter(Section.class, new JsonAdapter<Section>())
			.enableComplexMapKeySerialization()
			.setPrettyPrinting()
			.create();

	public static <T> void write(T ob, Writer writer) {
		PARSER.toJson(ob, ob.getClass(), writer);
	}

	public static <T> T read(Reader reader, Class<T> _class) {
		return PARSER.fromJson(reader, _class);
	}
}
