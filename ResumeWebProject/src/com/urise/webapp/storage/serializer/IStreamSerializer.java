package com.urise.webapp.storage.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.urise.webapp.model.Resume;

public interface IStreamSerializer {

	Resume doRead(InputStream in) throws IOException;

	void doWrite(Resume resume, OutputStream out) throws IOException;
}