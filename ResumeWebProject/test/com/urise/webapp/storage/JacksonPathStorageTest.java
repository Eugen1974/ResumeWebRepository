package com.urise.webapp.storage;

import java.io.File;
import java.nio.file.Path;

import com.urise.webapp.storage.serializer.JacksonStreamSerializer;

public class JacksonPathStorageTest extends AbstractFilePathStorageTest<Path> {

	public JacksonPathStorageTest() {
		super(new PathStorage(new File(ITestData.DIRECTORY_OF_RESUMES).toPath(), new JacksonStreamSerializer()));
	}
}
