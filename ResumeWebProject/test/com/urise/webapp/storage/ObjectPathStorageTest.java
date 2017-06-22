package com.urise.webapp.storage;

import java.io.File;
import java.nio.file.Path;

import com.urise.webapp.storage.serializer.ObjectStreamSerializer;

public class ObjectPathStorageTest extends AbstractFilePathStorageTest<Path> {

	public ObjectPathStorageTest() {
		super(new PathStorage(new File(ITestData.DIRECTORY_OF_RESUMES).toPath(), new ObjectStreamSerializer()));
	}
}
