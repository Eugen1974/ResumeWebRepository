package com.urise.webapp.storage;

import java.util.Objects;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {
	@Override
	protected void insertElement(Resume resume, int index) {
		storage[size] = resume;
	}

	@Override
	protected void fillDeletedElement(int index) {
		storage[index] = storage[size - 1]; // last element replaces deleted one
	}

	@Override
	public Integer getSearchKey(String uuid) {
		for (int i = 0; i < size; i++) {
			if (Objects.equals(storage[i].getUuid(), uuid)) {
				return i;
			}
		}
		return -1; // nothing
	}
}