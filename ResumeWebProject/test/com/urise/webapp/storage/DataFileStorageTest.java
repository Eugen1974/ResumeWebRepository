package com.urise.webapp.storage;

import java.io.File;

import com.urise.webapp.storage.serializer.DataStreamSerializer;

public class DataFileStorageTest extends AbstractFilePathStorageTest<File> {

	public DataFileStorageTest() {
		super(new FileStorage(new File(ITestData.DIRECTORY_OF_RESUMES), new DataStreamSerializer()));
	}
}
