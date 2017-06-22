package com.urise.webapp.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.urise.webapp.model.Resume;

public class MapStorage extends AbstractStorage<Integer> {

	private final Map<String, Resume> resumes;

	public MapStorage() {
		resumes = new HashMap<>();
	}

	public MapStorage(Map<String, Resume> resumes) {
		this.resumes = resumes;
	}

	@Override
	protected void doUpdate(Resume resume, Integer searchKey) {
		resumes.put(resume.getUuid(), resume);
	}

	@Override
	protected void doSave(Resume resume, Integer searchKey) {
		resumes.put(resume.getUuid(), resume);
	}

	@Override
	protected void doDelete(Integer searchKey) {
		Resume r = doGet(searchKey);
		resumes.remove(r.getUuid());
	}

	@Override
	public Integer getSearchKey(String uuid) {
		int i = 0;
		for (String key : resumes.keySet()) {
			if (Objects.equals(key, uuid)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	protected Resume doGet(Integer searchKey) {
		int i = 0;
		for (Entry<String, Resume> entry : resumes.entrySet()) {
			if (i == searchKey) {
				return entry.getValue();
			}
			i++;
		}
		return null;
	}

	@Override
	public void clear() {
		resumes.clear();
	}

	@Override
	public int getSize() {
		return resumes.size();
	}

	public Map<String, Resume> getMapResumes() {
		return resumes;
	}

	@Override
	public <SK> boolean isExist(SK searchKey) {
		Integer key = (Integer) searchKey;
		return key >= 0 && key < resumes.size();
	}

	@Override
	protected List<Resume> doCopyAll() {
		return new ArrayList<>(resumes.values());
	}
}
