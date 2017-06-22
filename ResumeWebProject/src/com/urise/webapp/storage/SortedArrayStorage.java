package com.urise.webapp.storage;

import java.util.Arrays;

import com.urise.webapp.model.Resume;

public class SortedArrayStorage extends AbstractArrayStorage {
	@Override
	protected void insertElement(Resume resume, int index) {
		// http://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array#answer-36239
		index = -index - 1;
		System.arraycopy(storage, index, storage, index + 1, LENGTH_STORAGE - index - 1);
		storage[index] = resume;
	}

	@Override
	protected void fillDeletedElement(int index) {
		// offset rest array to left on 1 element
		System.arraycopy(storage, index + 1, storage, index, size - index);
	}

	@Override
	public Integer getSearchKey(String uuid) {
		return Arrays.binarySearch(storage, 0, size, new Resume(uuid, "nothing"));
	}
}
