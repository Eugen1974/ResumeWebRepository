package com.urise.webapp.storage;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.urise.webapp.model.Resume;

public class MapStorageTest extends AbstractStorageTest<Integer> {

	public MapStorageTest() {
		super(new MapStorage());
	}

	@Test
	public void testGetMapResumes() {
		Map<String, Resume> resumes = ((MapStorage) storage).getMapResumes();
		Map<String, Resume> map = new HashMap<>();
		map.put(ITestData.UUID_2, ITestData.RESUME_2);
		map.put(ITestData.UUID_1, ITestData.RESUME_1);
		map.put(ITestData.UUID_0, ITestData.RESUME_0);
		assertEquals(map, resumes);
	}
}