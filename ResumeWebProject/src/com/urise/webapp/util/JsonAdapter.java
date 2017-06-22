package com.urise.webapp.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.urise.webapp.exception.StorageException;

public class JsonAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

	private static final String CLASSNAME = "CLASSNAME";
	private static final String INSTANCE = "INSTANCE";

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
		String className = primitive.getAsString();
		try {
			Class<?> _class = Class.forName(className);
			return context.deserialize(jsonObject.get(INSTANCE), _class);
		} catch (ClassNotFoundException e) {
			throw new StorageException("Error in method deserialize " + getClass().getName(), e);
		}
	}

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject retValue = new JsonObject();
		retValue.addProperty(CLASSNAME, src.getClass().getName());
		JsonElement element = context.serialize(src);
		retValue.add(INSTANCE, element);
		return retValue;
	}
}
