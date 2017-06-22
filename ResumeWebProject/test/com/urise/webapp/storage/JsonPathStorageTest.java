package com.urise.webapp.storage;

import java.io.File;
import java.nio.file.Path;

import com.urise.webapp.storage.serializer.JsonStreamSerializer;

public class JsonPathStorageTest extends AbstractFilePathStorageTest<Path> {

	public JsonPathStorageTest() {
		super(new PathStorage(new File(ITestData.DIRECTORY_OF_RESUMES).toPath(), new JsonStreamSerializer()));
	}
}
