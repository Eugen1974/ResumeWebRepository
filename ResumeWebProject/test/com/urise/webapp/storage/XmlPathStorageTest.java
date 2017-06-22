package com.urise.webapp.storage;

import java.io.File;
import java.nio.file.Path;

import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class XmlPathStorageTest extends AbstractFilePathStorageTest<Path> {

	public XmlPathStorageTest() {
		super(new PathStorage(new File(ITestData.DIRECTORY_OF_RESUMES).toPath(), new XmlStreamSerializer()));
	}
}
