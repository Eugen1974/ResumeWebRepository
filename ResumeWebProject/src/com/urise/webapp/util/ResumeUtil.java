package com.urise.webapp.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;

public class ResumeUtil {

	private ResumeUtil() {
	}

	public static void setResumeWithSortedContactsAndSections(Resume resume) {
		Objects.requireNonNull(resume, "resume is null !");
		Map<ContactType, String> oldContacts = resume.getContacts();
		Objects.requireNonNull(oldContacts, "oldContacts is null !");
		Map<ContactType, String> newContacts = new LinkedHashMap<>();
		setContact(oldContacts, newContacts, ContactType.HOME_ADDRESS);
		setContact(oldContacts, newContacts, ContactType.HOME_PAGE);
		setContact(oldContacts, newContacts, ContactType.HOME_PHONE);
		setContact(oldContacts, newContacts, ContactType.MOBILE);
		setContact(oldContacts, newContacts, ContactType.MAIL);
		setContact(oldContacts, newContacts, ContactType.SKYPE);
		setContact(oldContacts, newContacts, ContactType.HABRAHABR);
		setContact(oldContacts, newContacts, ContactType.STACKOVERFLOW);
		setContact(oldContacts, newContacts, ContactType.GITHUB);
		setContact(oldContacts, newContacts, ContactType.LINKEDIN);
		resume.setContacts(newContacts);

		Map<SectionType, Section> oldSections = resume.getSections();
		Objects.requireNonNull(oldSections, "oldSections is null !");
		Map<SectionType, Section> newSections = new LinkedHashMap<>();
		setSection(oldSections, newSections, SectionType.PERSONAL);
		setSection(oldSections, newSections, SectionType.OBJECTIVE);
		setSection(oldSections, newSections, SectionType.ACHIEVEMENT);
		setSection(oldSections, newSections, SectionType.QUALIFICATIONS);
		setSection(oldSections, newSections, SectionType.EXPERIENCE);
		setSection(oldSections, newSections, SectionType.EDUCATION);
		resume.setSections(newSections);
	}

	private static void setContact(Map<ContactType, String> oldContacts, Map<ContactType, String> newContacts,
			ContactType contactType) {
		if (oldContacts.containsKey(contactType)) {
			newContacts.put(contactType, oldContacts.get(contactType));
		}
	}

	private static void setSection(Map<SectionType, Section> oldSections, Map<SectionType, Section> newSections,
			SectionType sectionType) {
		if (oldSections.containsKey(sectionType)) {
			newSections.put(sectionType, oldSections.get(sectionType));
		}
	}
}
