package com.urise.webapp.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.IStreamSerializer;

public class PathStorage extends AbstractFilePathStorage<Path> {

	public PathStorage(Path directory, IStreamSerializer streamSerializer) {
		super(directory, streamSerializer);
		if (!Files.isDirectory(directory)) {
			throw new StorageException(new IllegalArgumentException(directory + " is not directory !"));
		}
		if (Files.notExists(directory)) {
			throw new StorageException(new IllegalArgumentException("directory " + directory + " does not exist !"));
		}
		if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
			throw new StorageException(new IllegalArgumentException(directory + " cannot read/write !"));
		}
	}

	@Override
	protected void doDelete(Path path) {
		try {
			Files.delete(path);
		} catch (IOException e) {
			throw new StorageException("Error in method doDelete.", path.toString(), e);
		}
	}

	@Override
	protected void doUpdate(Resume resume, Path path) {
		try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path))) {
			streamSerializer.doWrite(resume, out);
		} catch (IOException e) {
			throw new StorageException("Error in method doUpdate.", resume.getUuid(), e);
		}
	}

	@Override
	protected void doSave(Resume resume, Path path) {
		if (Files.notExists(path)) {
			try {
				Files.createFile(path);
				doUpdate(resume, path);
			} catch (IOException e) {
				throw new StorageException("Error in method doSave.", resume.getUuid(), e);
			}
		}
	}

	@Override
	protected Resume doGet(Path path) {
		try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
			return streamSerializer.doRead(in);
		} catch (IOException e) {
			throw new StorageException("Error in method doGet.", path.toString(), e);
		}
	}

	@Override
	protected List<Resume> doCopyAll() {
		return getFilesList().map(this::doGet).collect(Collectors.toList());
	}

	@Override
	public Path getSearchKey(String nameFile) {
		return new File(directory.toString(), nameFile).toPath();
	}

	@Override
	public <SK> boolean isExist(SK path) {
		Path path_ = (Path) path;
		return Files.exists(path_) && Objects.equals(path_.getParent(), directory);
	}

	@Override
	public int getSize() {
		return (int) getFilesList().count();
	}

	@Override
	public void clear() {
		getFilesList().forEach(this::doDelete);
	}

	private Stream<Path> getFilesList() {
		try {
			return Files.list(directory);
		} catch (IOException e) {
			throw new StorageException("Error in method getFileList", e);
		}
	}
}