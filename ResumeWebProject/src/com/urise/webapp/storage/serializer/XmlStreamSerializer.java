package com.urise.webapp.storage.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBException;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Link;
import com.urise.webapp.model.ListSection;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.OrganizationSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.util.XmlParser;

public class XmlStreamSerializer implements IStreamSerializer {

	private static final XmlParser<Resume> XML_PARSER;

	static {
		try {
			XML_PARSER = new XmlParser<>(Resume.class, TextSection.class, ListSection.class, OrganizationSection.class,
					Organization.class, Organization.Position.class, Link.class);
		} catch (JAXBException e) {
			throw new StorageException("Error in static block " + XmlStreamSerializer.class.getName(), e);
		}
	}

	@Override
	public Resume doRead(InputStream in) {
		try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			return XML_PARSER.read(reader);
		} catch (JAXBException | IOException e) {
			throw new StorageException("Error in method doRead " + getClass().getName(), e);
		}
	}

	@Override
	public void doWrite(Resume resume, OutputStream out) {
		try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
			XML_PARSER.write(resume, writer);
		} catch (JAXBException | IOException e) {
			throw new StorageException("Error in method doWrite " + getClass().getName(), resume.getUuid(), e);
		}
	}
}
