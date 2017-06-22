package com.urise.webapp.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

	public static final int LENGTH_STORAGE = 10_000;

	protected final Resume[] storage;
	protected int size;

	protected AbstractArrayStorage() {
		storage = new Resume[LENGTH_STORAGE];
		size = 0;
	}

	protected abstract void insertElement(Resume resume, int index);

	protected abstract void fillDeletedElement(int index);

	@Override
	protected void doUpdate(Resume resume, Integer searchKey) {
		storage[searchKey] = resume;
	}

	@Override
	protected Resume doGet(Integer searchKey) {
		return storage[searchKey];
	}

	@Override
	public void clear() {
		Arrays.fill(storage, 0, size, null);
		size = 0;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	protected void doSave(Resume resume, Integer searchKey) {
		if (size >= LENGTH_STORAGE) {
			throw new StorageException("ERROR: Array resumes is full !", resume.getUuid());
		}
		insertElement(resume, searchKey);
		size++;
	}

	@Override
	protected void doDelete(Integer searchKey) {
		fillDeletedElement(searchKey);
		storage[size - 1] = null; // last element became null
		size--;
	}

	@Override
	public <SK> boolean isExist(SK searchKey) {
		Integer key = (Integer) searchKey;
		return key >= 0 && key < size;
	}

	@Override
	protected List<Resume> doCopyAll() {
		Resume[] temp = Arrays.copyOf(storage, size);
		return new ArrayList<>(Arrays.asList(temp));
	}
}
