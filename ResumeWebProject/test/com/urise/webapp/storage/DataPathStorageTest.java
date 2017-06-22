package com.urise.webapp.storage;

import java.io.File;
import java.nio.file.Path;

import com.urise.webapp.storage.serializer.DataStreamSerializer;

public class DataPathStorageTest extends AbstractFilePathStorageTest<Path> {

	public DataPathStorageTest() {
		super(new PathStorage(new File(ITestData.DIRECTORY_OF_RESUMES).toPath(), new DataStreamSerializer()));
	}
}
