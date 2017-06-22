package com.urise.webapp.storage;

import java.util.Objects;

import com.urise.webapp.storage.serializer.IStreamSerializer;

public abstract class AbstractFilePathStorage<SK> extends AbstractStorage<SK> {

	protected final SK directory;
	protected final IStreamSerializer streamSerializer;

	protected AbstractFilePathStorage(SK directory, IStreamSerializer streamSerializer) {
		Objects.requireNonNull(directory, "directory is null !");
		Objects.requireNonNull(streamSerializer, "streamSerializer is null !");
		this.directory = directory;
		this.streamSerializer = streamSerializer;
	}

	public SK getDirectory() {
		return directory;
	}

	public IStreamSerializer getStreamSerializer() {
		return streamSerializer;
	}
}
