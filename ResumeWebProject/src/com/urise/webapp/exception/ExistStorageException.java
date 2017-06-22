package com.urise.webapp.exception;

public class ExistStorageException extends StorageException {

	private static final long serialVersionUID = 1L;

	public ExistStorageException(String uuid) {
		super("ERROR: Resume with uuid=" + uuid + " is present !", uuid);
	}

	public ExistStorageException(String message, String uuid) {
		super(message, uuid);
	}

	public ExistStorageException(String message, String uuid, Exception e) {
		super(message, uuid, e);
	}
}
