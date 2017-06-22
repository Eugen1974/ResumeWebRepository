package com.urise.webapp.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.urise.webapp.model.Resume;

public class ListStorage extends AbstractStorage<Integer> {

	private final List<Resume> resumes;

	public ListStorage() {
		resumes = new ArrayList<>();
	}

	public ListStorage(List<Resume> resumes) {
		this.resumes = resumes;
	}

	@Override
	protected void doUpdate(Resume resume, Integer searchKey) {
		resumes.set(searchKey, resume);
	}

	@Override
	protected void doSave(Resume resume, Integer searchKey) {
		resumes.add(resume);
	}

	@Override
	protected void doDelete(Integer searchKey) {
		resumes.remove(searchKey.intValue());
	}

	@Override
	protected Resume doGet(Integer searchKey) {
		return resumes.get(searchKey);
	}

	@Override
	public Integer getSearchKey(String uuid) {
		for (int i = 0; i < resumes.size(); i++) {
			if (Objects.equals(resumes.get(i).getUuid(), uuid)) {
				return i;
			}
		}
		return -1;
	}

	public List<Resume> getListResumes() {
		return resumes;
	}

	@Override
	public void clear() {
		resumes.clear();
	}

	@Override
	public int getSize() {
		return resumes.size();
	}

	@Override
	public <SK> boolean isExist(SK searchKey) {
		Integer key = (Integer) searchKey;
		return key >= 0 && key < resumes.size();
	}

	@Override
	protected List<Resume> doCopyAll() {
		return new ArrayList<>(resumes);
	}
}
