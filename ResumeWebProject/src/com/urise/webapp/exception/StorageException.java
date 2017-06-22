package com.urise.webapp.exception;

import java.util.logging.Level;

import com.urise.webapp.util.ResumeLogger;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String uuid;

	public StorageException(Exception e) {
		this(e.getMessage(), null, e);
	}

	public StorageException(String message) {
		this(message, null, null);
	}

	public StorageException(String message, String uuid) {
		this(message, uuid, null);
	}

	public StorageException(String message, Exception e) {
		this(message, null, e);
	}

	public StorageException(String message, String uuid, Exception e) {
		super(message, e);
		this.uuid = uuid;
		ResumeLogger.getInstance().getLogger().log(Level.WARNING, message, e);
	}

	public String getUuid() {
		return uuid;
	}
}
