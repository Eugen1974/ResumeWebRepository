package com.urise.webapp.storage;

import java.io.File;

import com.urise.webapp.storage.serializer.JsonStreamSerializer;

public class JsonFileStorageTest extends AbstractFilePathStorageTest<File> {

	public JsonFileStorageTest() {
		super(new FileStorage(new File(ITestData.DIRECTORY_OF_RESUMES), new JsonStreamSerializer()));
	}
}
