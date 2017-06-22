package com.urise.webapp.storage;

import java.util.List;

import com.urise.webapp.model.Resume;

public interface IStorage {

	boolean update(Resume resume);

	boolean save(Resume resume);

	boolean delete(String uuid);

	Resume get(String uuid);

	void clear();

	int getSize();

	List<Resume> getAllSorted();

	<SK> boolean isExist(SK searchKey);
}