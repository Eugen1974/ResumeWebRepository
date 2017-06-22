package com.urise.webapp.util;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlParser<T> {

	private final JAXBContext context;

	public XmlParser(Class<?>... classes) throws JAXBException {
		Objects.requireNonNull(classes, "classes is null !");
		context = JAXBContext.newInstance(classes);
	}

	@SuppressWarnings("unchecked")
	public T read(Reader in) throws JAXBException {
		return (T) context.createUnmarshaller().unmarshal(in);
	}

	public void write(T ob, Writer out) throws JAXBException {
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // pretty
																		// output
		marshaller.marshal(ob, out);
	}
}
