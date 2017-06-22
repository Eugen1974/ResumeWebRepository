package com.urise.webapp.storage;

import java.io.File;

import com.urise.webapp.storage.serializer.JacksonStreamSerializer;

public class JacksonFileStorageTest extends AbstractFilePathStorageTest<File> {

	public JacksonFileStorageTest() {
		super(new FileStorage(new File(ITestData.DIRECTORY_OF_RESUMES), new JacksonStreamSerializer()));
	}
}
