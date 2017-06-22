package com.urise.webapp.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {

	private static final long serialVersionUID = 1L;

	public static final Resume EMPTY;
	static {
		Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
		for (ContactType contactType : ContactType.values()) {
			contacts.put(contactType, new String());
		}

		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
		sections.put(SectionType.PERSONAL, TextSection.EMPTY);
		sections.put(SectionType.OBJECTIVE, TextSection.EMPTY);
		sections.put(SectionType.ACHIEVEMENT, ListSection.EMPTY);
		sections.put(SectionType.QUALIFICATIONS, ListSection.EMPTY);
		sections.put(SectionType.EXPERIENCE, OrganizationSection.EMPTY);
		sections.put(SectionType.EDUCATION, OrganizationSection.EMPTY);

		EMPTY = new Resume();
		EMPTY.contacts = Collections.unmodifiableMap(contacts);
		EMPTY.sections = Collections.unmodifiableMap(sections);
	}

	@JsonProperty
	private final String uuid;
	@JsonProperty
	private final String fullName;
	@JsonProperty
	private Map<ContactType, String> contacts;
	@JsonProperty
	private Map<SectionType, Section> sections;

	public Resume() {
		this(UUID.randomUUID().toString(), new String(), new EnumMap<>(ContactType.class),
				new EnumMap<>(SectionType.class));
	}

	public Resume(String fullName) {
		this(UUID.randomUUID().toString(), fullName, new EnumMap<>(ContactType.class),
				new EnumMap<>(SectionType.class));
	}

	public Resume(String uuid, String fullName) {
		this(uuid, fullName, new EnumMap<>(ContactType.class), new EnumMap<>(SectionType.class));
	}

	public Resume(String fullName, Map<ContactType, String> contacts, Map<SectionType, Section> sections) {
		this(UUID.randomUUID().toString(), fullName, contacts, sections);
	}

	public Resume(String uuid, String fullName, Map<ContactType, String> contacts) {
		this(uuid, fullName, contacts, new EnumMap<>(SectionType.class));
	}

	public Resume(String uuid, String fullName, Map<ContactType, String> contacts, Map<SectionType, Section> sections) {
		Objects.requireNonNull(uuid, "uuid is null !");
		Objects.requireNonNull(fullName, "fullName is null !");
		Objects.requireNonNull(contacts, "contacts is null !");
		Objects.requireNonNull(sections, "sections is null !");
		this.uuid = uuid;
		this.fullName = fullName;
		this.contacts = contacts;

		checkSetSections(sections);
		this.sections = sections;
	}

	public void setContact(ContactType contactType, String contact) {
		checkSetEMPTY("contact");
		contacts.put(contactType, contact);
	}

	public void setSection(SectionType sectionType, Section section) {
		checkSetEMPTY("section");
		checkSetSection(sectionType, section);
		sections.put(sectionType, section);
	}

	public String getUuid() {
		return uuid;
	}

	public String getFullName() {
		return fullName;
	}

	public Map<ContactType, String> getContacts() {
		return contacts;
	}

	public void setContacts(Map<ContactType, String> contacts) {
		checkSetEMPTY("contacts");
		this.contacts = contacts;
	}

	public Map<SectionType, Section> getSections() {
		return sections;
	}

	public void setSections(Map<SectionType, Section> sections) {
		checkSetEMPTY("sections");
		checkSetSections(sections);
		this.sections = sections;
	}

	public String getContact(ContactType contactType) {
		return contacts.get(contactType);
	}

	public Section getSection(SectionType sectionType) {
		return sections.get(sectionType);
	}

	@Override
	public String toString() {
		return "Resume [uuid=" + uuid + ", fullName=" + fullName + ", contacts=" + contacts + ", sections=" + sections
				+ "]";
	}

	@Override
	public boolean equals(Object resume) {
		if (resume instanceof Resume) {
			Resume r = (Resume) resume;
			return Objects.equals(uuid, r.uuid) && Objects.equals(fullName, r.fullName)
					&& Objects.equals(contacts, r.contacts) && Objects.equals(sections, r.sections);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, fullName, contacts, sections);
	}

	@Override
	public int compareTo(Resume o) {
		return uuid.compareTo(o.uuid);
	}

	@Override
	public Resume clone() {
		Map<SectionType, Section> tempSections = new EnumMap<>(SectionType.class);
		sections.forEach((key, value) -> tempSections.put(key, value.clone()));
		return new Resume(uuid, fullName, new EnumMap<>(contacts), tempSections);
	}

	private void checkSetEMPTY(String nameField) {
		if (Objects.equals(this, EMPTY)) {
			throw new UnsupportedOperationException(
					"Invalid operation with " + nameField + " for object Resume.EMPTY !");
		}
	}

	private void checkSetSections(Map<SectionType, Section> sections) {
		Objects.requireNonNull(sections, "sections is null !");
		sections.forEach((key, value) -> checkSetSection(key, value));
	}

	private void checkSetSection(SectionType sectionType, Section section) {
		Objects.requireNonNull(sectionType, "sectionType is null !");
		Objects.requireNonNull(section, "section is null !");
		String message = "Wrong type section " + section.getClass().getName() + " for sections ";
		if ((Objects.equals(sectionType, SectionType.PERSONAL) || Objects.equals(sectionType, SectionType.OBJECTIVE))
				&& !(section instanceof TextSection)) {
			throw new IllegalArgumentException(message + "PERSONAL & OBJECTIVE. Must be type TextSection");
		}
		if ((Objects.equals(sectionType, SectionType.ACHIEVEMENT)
				|| Objects.equals(sectionType, SectionType.QUALIFICATIONS)) && !(section instanceof ListSection)) {
			throw new IllegalArgumentException(message + "ACHIEVEMENT & QUALIFICATIONS. Must be type ListSection");
		}
		if ((Objects.equals(sectionType, SectionType.EXPERIENCE) || Objects.equals(sectionType, SectionType.EDUCATION))
				&& !(section instanceof OrganizationSection)) {
			throw new IllegalArgumentException(message + "EXPERIENCE & EDUCATION. Must be type OrganizationSection");
		}
	}
}