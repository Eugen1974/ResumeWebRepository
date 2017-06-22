package com.urise.webapp.storage;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;

public class SqlStorageTest extends AbstractStorageTest<String> {

	public SqlStorageTest() {
		super(new SqlStorage());
	}

	@Test
	public void testGetContactsByUuid() {
		Map<ContactType, String> contacts = ((SqlStorage) storage).getContactsByUuid(ITestData.UUID_2);
		assertEquals(ITestData.RESUME_2.getContacts(), contacts);
	}

	@Test
	public void testGetSectionsByUuid() {
		Map<SectionType, Section> sections = ((SqlStorage) storage).getSectionsByUuid(ITestData.UUID_2);
		assertEquals(ITestData.RESUME_2.getSections(), sections);
	}
}
