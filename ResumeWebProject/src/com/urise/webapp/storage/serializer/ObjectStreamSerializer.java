package com.urise.webapp.storage.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

public class ObjectStreamSerializer implements IStreamSerializer {

	@Override
	public Resume doRead(InputStream in) throws IOException {
		try (ObjectInputStream objectStream = new ObjectInputStream(in)) {
			return (Resume) objectStream.readObject();
		} catch (ClassNotFoundException e) {
			throw new StorageException("Error in method doRead", e);
		}
	}

	@Override
	public void doWrite(Resume resume, OutputStream out) throws IOException {
		try (ObjectOutputStream objectStream = new ObjectOutputStream(out)) {
			objectStream.writeObject(resume);
			objectStream.flush();
		}
	}
}
