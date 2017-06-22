package com.urise.webapp.storage;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.urise.webapp.model.Resume;

public class ListStorageTest extends AbstractStorageTest<Integer> {

	public ListStorageTest() {
		super(new ListStorage());
	}

	@Test
	public void testGetListResumes() {
		List<Resume> resumes = ((ListStorage) storage).getListResumes();
		List<Resume> list = Arrays.asList(ITestData.RESUME_2, ITestData.RESUME_0, ITestData.RESUME_1);
		assertEquals(list, resumes);
	}
}