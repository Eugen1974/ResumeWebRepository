package com.urise.webapp.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.junit.Test;

@SuppressWarnings("unchecked")
public abstract class AbstractFilePathStorageTest<SK> extends AbstractStorageTest<SK> {

	public AbstractFilePathStorageTest(IStorage storage) {
		super(storage);
	}

	@Test
	public void testGetDirectory() {
		File dir = new File(ITestData.DIRECTORY_OF_RESUMES);
		SK directory = ((AbstractFilePathStorage<SK>) storage).getDirectory();
		if (storage instanceof FileStorage) {
			assertEquals(dir, directory);
		} else if (storage instanceof PathStorage) {
			assertEquals(dir.toPath(), directory);
		} else {
			fail("Error in method testGetDirectory() for " + storage.getClass().getName());
		}
	}

	@Test
	public void testGetStreamSerializer() {
		assertNotNull(((AbstractFilePathStorage<SK>) storage).getStreamSerializer());
	}

	@Test
	public void testDoRead() {
		AbstractFilePathStorage<SK> st = (AbstractFilePathStorage<SK>) storage;
		try {
			if (storage instanceof FileStorage) {
				try (InputStream in = new BufferedInputStream(new FileInputStream(ITestData.FILE_2))) {
					partDoRead(in, st);
				}
			} else if (storage instanceof PathStorage) {
				try (InputStream in = new BufferedInputStream(Files.newInputStream(ITestData.FILE_2.toPath()))) {
					partDoRead(in, st);
				}
			} else {
				fail("Doesn't processed case for type " + storage.getClass().getName());
			}
		} catch (IOException e) {
			fail("Error in method testDoRead. " + e.getMessage());
		}
	}

	@Test
	public void testDoWrite() {
		AbstractFilePathStorage<SK> st = (AbstractFilePathStorage<SK>) storage;
		try {
			if (storage instanceof FileStorage) {
				try (OutputStream out = new BufferedOutputStream(new FileOutputStream(ITestData.FILE_3));
						InputStream in = new BufferedInputStream(new FileInputStream(ITestData.FILE_3))) {
					partDoWrite(out, in, st);
				}
			} else if (storage instanceof PathStorage) {
				try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(ITestData.FILE_3.toPath()));
						InputStream in = new BufferedInputStream(Files.newInputStream(ITestData.FILE_3.toPath()))) {
					partDoWrite(out, in, st);
				}
			} else {
				fail("Doesn't processed case for type " + storage.getClass().getName());
			}
		} catch (IOException e) {
			fail("Error in method testDoWrite. " + e.getMessage());
		}
	}

	private void partDoRead(InputStream in, AbstractFilePathStorage<SK> st) throws IOException {
		assertEquals(ITestData.RESUME_2, st.streamSerializer.doRead(in));
	}

	private void partDoWrite(OutputStream out, InputStream in, AbstractFilePathStorage<SK> st) throws IOException {
		st.streamSerializer.doWrite(ITestData.RESUME_3, out);
		out.close(); // stream need to close before reading the object
		assertEquals(ITestData.RESUME_3, st.streamSerializer.doRead(in));
		assertEquals(4, storage.getSize());
	}
}
