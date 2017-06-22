package com.urise.webapp.storage;

import java.io.File;

import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class XmlFileStorageTest extends AbstractFilePathStorageTest<File> {

	public XmlFileStorageTest() {
		super(new FileStorage(new File(ITestData.DIRECTORY_OF_RESUMES), new XmlStreamSerializer()));
	}
}
