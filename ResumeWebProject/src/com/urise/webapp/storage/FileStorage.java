package com.urise.webapp.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.IStreamSerializer;

public class FileStorage extends AbstractFilePathStorage<File> {

	public FileStorage(File directory, IStreamSerializer streamSerializer) {
		super(directory, streamSerializer);
		if (directory.isFile()) {
			throw new StorageException(new IllegalArgumentException(directory.getAbsolutePath() + " is file !"));
		}
		if (!directory.exists()) {
			throw new StorageException(
					new IllegalArgumentException("directory " + directory.getAbsolutePath() + " does not exist !"));
		}
		if (!directory.canRead() || !directory.canWrite()) {
			throw new StorageException(
					new IllegalArgumentException(directory.getAbsolutePath() + " cannot read/write !"));
		}
	}

	@Override
	protected void doDelete(File file) {
		if (!file.delete()) {
			throw new StorageException("Cannot delete " + file.getName() + " !", file.getName());
		}
	}

	@Override
	protected void doUpdate(Resume resume, File file) {
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
			streamSerializer.doWrite(resume, out);
		} catch (IOException e) {
			throw new StorageException("Error in method doUpdate.", file.getName(), e);
		}
	}

	@Override
	protected void doSave(Resume resume, File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
				doUpdate(resume, file);
			} catch (IOException e) {
				throw new StorageException("Error in method doSave", file.getName(), e);
			}
		}
	}

	@Override
	protected Resume doGet(File file) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
			return streamSerializer.doRead(in);
		} catch (IOException e) {
			throw new StorageException("Error in method doGet.", file.getName(), e);
		}
	}

	@Override
	protected List<Resume> doCopyAll() {
		List<Resume> list = new ArrayList<>();
		for (File file : directory.listFiles()) {
			try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
				list.add(streamSerializer.doRead(in));
			} catch (IOException e) {
				throw new StorageException("Error in method doCopyAll.", file.getName(), e);
			}
		}
		return list;
	}

	@Override
	public File getSearchKey(String nameFile) {
		return new File(directory, nameFile);
	}

	@Override
	public <SK> boolean isExist(SK file) {
		File file_ = (File) file;
		return file_.exists() && Objects.equals(file_.getParentFile().getName(), directory.getName());
	}

	@Override
	public int getSize() {
		return directory.list().length;
	}

	@Override
	public void clear() {
		File[] files = directory.listFiles();
		if (Objects.nonNull(files)) {
			for (File file : files) {
				doDelete(file);
			}
		}
	}
}