package com.urise.webapp.storage;

import java.util.Comparator;
import java.util.List;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage<SK> implements IStorage {

	public static final Comparator<Resume> RESUME_COMPARATOR_BY_FULL_NAME;
	static {
		RESUME_COMPARATOR_BY_FULL_NAME = (o1, o2) -> o1.getFullName().compareToIgnoreCase(o2.getFullName());
	}

	protected abstract SK getSearchKey(String uid);

	protected abstract void doDelete(SK searchKey);

	protected abstract void doUpdate(Resume resume, SK searchKey);

	protected abstract void doSave(Resume resume, SK searchKey);

	protected abstract Resume doGet(SK searchKey);

	protected abstract List<Resume> doCopyAll();

	@Override
	public boolean update(Resume resume) {
		SK searchKey = getExistedSearchKey(resume.getUuid());
		doUpdate(resume, searchKey);
		return true;
	}

	@Override
	public boolean save(Resume resume) {
		SK searchKey = getNotExistedSearchKey(resume.getUuid());
		doSave(resume, searchKey);
		return true;
	}

	@Override
	public boolean delete(String uuid) {
		SK searchKey = getExistedSearchKey(uuid);
		doDelete(searchKey);
		return true;
	}

	@Override
	public Resume get(String uuid) {
		SK searchKey = getExistedSearchKey(uuid);
		return doGet(searchKey);
	}

	private SK getExistedSearchKey(String uuid) {
		SK searchKey = getSearchKey(uuid);
		if (!isExist(searchKey)) {
			throw new NotExistStorageException(uuid);
		}
		return searchKey;
	}

	private SK getNotExistedSearchKey(String uuid) {
		SK searchKey = getSearchKey(uuid);
		if (isExist(searchKey)) {
			throw new ExistStorageException(uuid);
		}
		return searchKey;
	}

	@Override
	public List<Resume> getAllSorted() {
		List<Resume> list = doCopyAll();
		list.sort(AbstractStorage.RESUME_COMPARATOR_BY_FULL_NAME);
		return list;
	}
}
