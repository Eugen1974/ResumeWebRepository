package com.urise.webapp.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorageTest<SK> {

	protected final IStorage storage;

	public AbstractStorageTest(IStorage storage) {
		this.storage = storage;
	}

	@Before
	public void before() {
		storage.clear();
		storage.save(ITestData.RESUME_2);
		storage.save(ITestData.RESUME_0);
		storage.save(ITestData.RESUME_1);
	}

	@Test
	public void testUpdate() {
		Resume resume = new Resume(ITestData.UUID_1, ITestData.FULL_NAME_3, ITestData.RESUME_0.getContacts(),
				ITestData.RESUME_2.getSections());
		assertTrue(storage.update(resume));
		assertEquals(resume, storage.get(ITestData.UUID_1));
	}

	@Test(expected = NotExistStorageException.class)
	public void testUpdateError() {
		storage.update(ITestData.RESUME_3);
	}

	@Test
	public void testSave() {
		assertTrue(storage.save(ITestData.RESUME_3));
		assertEquals(ITestData.RESUME_3, storage.get(ITestData.UUID_3));
		assertEquals(4, storage.getSize());
	}

	// SQLException java.sql.SQLIntegrityConstraintViolationException: ORA-00001:
	// нарушено ограничение уникальности (DEV.PK_RESUME) wrapping in
	// StorageException
	@Test(expected = StorageException.class)
	public void testSaveError() {
		storage.save(ITestData.RESUME_1);
	}

	@Test
	public void testDelete() {
		assertTrue(storage.delete(ITestData.UUID_2));
		assertEquals(2, storage.getSize());
	}

	@Test(expected = NotExistStorageException.class)
	public void testDeleteError() {
		storage.delete(ITestData.UUID_3);
	}

	@Test
	public void testGet() {
		assertEquals(ITestData.RESUME_0, storage.get(ITestData.UUID_0));
	}

	@Test
	public void testGetAllSorted() {
		assertEquals(Arrays.asList(ITestData.RESUME_2, ITestData.RESUME_0, ITestData.RESUME_1), storage.getAllSorted());
	}

	@Test
	public void testGetSize() {
		assertEquals(3, storage.getSize());
	}

	@Test
	public void testClear() {
		storage.clear();
		assertEquals(0, storage.getSize());
	}

	@Test(expected = NotExistStorageException.class)
	public void testGetError() {
		storage.get(ITestData.UUID_3);
	}

	@Test
	public void testIsExist() {
		Object ob1 = new Object();
		Object ob2 = new Object();
		if (storage instanceof SqlStorage) {
			ob1 = ITestData.UUID_1;
			ob2 = ITestData.UUID_3;
		} else if (storage instanceof AbstractArrayStorage || storage instanceof ListStorage
				|| storage instanceof MapStorage) {
			ob1 = 1;
			ob2 = 3;
		} else if (storage instanceof FileStorage) {
			ob1 = ITestData.FILE_1;
			ob2 = ITestData.FILE_3;
		} else if (storage instanceof PathStorage) {
			ob1 = ITestData.FILE_1.toPath();
			ob2 = ITestData.FILE_3.toPath();
		} else {
			fail("Doesn't processed case for " + storage.getClass().getName() + " in method testIsExist() !");
		}
		assertTrue(storage.isExist(ob1));
		assertFalse(storage.isExist(ob2));
	}
}