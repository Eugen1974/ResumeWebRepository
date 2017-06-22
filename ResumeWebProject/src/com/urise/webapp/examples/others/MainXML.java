package com.urise.webapp.examples.others;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBException;

import com.urise.webapp.model.Link;
import com.urise.webapp.model.ListSection;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.OrganizationSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.storage.ITestData;
import com.urise.webapp.util.XmlParser;

public class MainXML {

	public static final String NAME_FILE = "e:\\a.xml";

	public static void main(String[] args) throws JAXBException, IOException {
		XmlParser<Resume> parser = new XmlParser<>(Resume.class, TextSection.class, ListSection.class,
				OrganizationSection.class, Organization.class, Organization.Position.class, Link.class);

		try (Writer out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(NAME_FILE), StandardCharsets.UTF_8))) {
			parser.write(ITestData.RESUME_2, out);
			parser.write(ITestData.RESUME_2, new PrintWriter(System.out));
		}

		try (Reader in = new BufferedReader(
				new InputStreamReader(new FileInputStream(NAME_FILE), StandardCharsets.UTF_8))) {
			Resume ob = parser.read(in);
			System.out.println("\n" + ob);
		}
	}
}
