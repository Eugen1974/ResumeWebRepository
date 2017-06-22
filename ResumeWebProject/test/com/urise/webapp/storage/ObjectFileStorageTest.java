package com.urise.webapp.storage;

import java.io.File;

import com.urise.webapp.storage.serializer.ObjectStreamSerializer;

public class ObjectFileStorageTest extends AbstractFilePathStorageTest<File> {

	public ObjectFileStorageTest() {
		super(new FileStorage(new File(ITestData.DIRECTORY_OF_RESUMES), new ObjectStreamSerializer()));
	}
}
