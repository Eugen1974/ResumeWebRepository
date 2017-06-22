package com.urise.webapp.exception;

public class NotExistStorageException extends StorageException {

	private static final long serialVersionUID = 1L;

	public NotExistStorageException(String uuid) {
		super("ERROR: Resume with uuid=" + uuid + " doesn't present !", uuid);
	}

	public NotExistStorageException(String message, String uuid) {
		super(message, uuid);
	}

	public NotExistStorageException(String message, String uuid, Exception e) {
		super(message, uuid, e);
	}
}
