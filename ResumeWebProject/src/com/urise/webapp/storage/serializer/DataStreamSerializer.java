package com.urise.webapp.storage.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Link;
import com.urise.webapp.model.ListSection;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.Organization.Position;
import com.urise.webapp.model.OrganizationSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.model.TextSection;

public class DataStreamSerializer implements IStreamSerializer {

	private static final String TEXT_SECTION = "TEXT_SECTION";
	private static final String LIST_SECTION = "LIST_SECTION";
	private static final String ORGANIZATION_SECTION = "ORGANIZATION_SECTION";

	@Override
	public Resume doRead(InputStream in) throws IOException {
		String uuid = null;
		String fullName = null;
		Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
		try (DataInputStream reader = new DataInputStream(in)) {
			uuid = reader.readUTF();
			fullName = reader.readUTF();

			int amountContacts = reader.readInt();
			for (int i = 0; i < amountContacts; i++) {
				contacts.put(ContactType.valueOf(reader.readUTF()), reader.readUTF());
			}

			int amountSections = reader.readInt();
			for (int i = 0; i < amountSections; i++) {
				String nameSection = reader.readUTF();
				String marker = reader.readUTF();
				if (Objects.equals(marker, TEXT_SECTION)) {
					sections.put(SectionType.valueOf(nameSection), new TextSection(reader.readUTF()));
				} else if (Objects.equals(marker, LIST_SECTION)) {
					List<String> items = new ArrayList<>();
					int amountItems = reader.readInt();
					for (int j = 0; j < amountItems; j++) {
						items.add(reader.readUTF());
					}
					sections.put(SectionType.valueOf(nameSection), new ListSection(items));
				} else if (Objects.equals(marker, ORGANIZATION_SECTION)) {
					List<Organization> organizations = new ArrayList<>();
					int amountOrganizations = reader.readInt();
					for (int k = 0; k < amountOrganizations; k++) {
						Link homePage = new Link(reader.readUTF(), reader.readUTF());
						List<Position> positions = new ArrayList<>();
						int amountPositions = reader.readInt();
						for (int m = 0; m < amountPositions; m++) {
							LocalDate startDate = LocalDate.parse(reader.readUTF());
							LocalDate endDate = LocalDate.parse(reader.readUTF());
							String title = reader.readUTF();

							List<String> descriptions = new ArrayList<>();
							int amountDescriptions = reader.readInt();
							for (int n = 0; n < amountDescriptions; n++) {
								descriptions.add(reader.readUTF());
							}
							positions.add(new Position(startDate, endDate, title, descriptions));
						}
						organizations.add(new Organization(homePage, positions));
					}
					sections.put(SectionType.valueOf(nameSection), new OrganizationSection(organizations));
				} else {
					throw new StorageException("Not handled case for " + marker);
				}
			}
		}
		return new Resume(uuid, fullName, contacts, sections);
	}

	@Override
	public void doWrite(Resume resume, OutputStream out) throws IOException {
		try (DataOutputStream writer = new DataOutputStream(out)) {
			writer.writeUTF(resume.getUuid());
			writer.writeUTF(resume.getFullName());

			Map<ContactType, String> contacts = resume.getContacts();
			writer.writeInt(contacts.size());
			contacts.forEach((contactType, value) -> {
				try {
					writer.writeUTF(contactType.name());
					writer.writeUTF(value);
				} catch (IOException e) {
					throw new StorageException("Error in method doWrite", e);
				}
			});

			Map<SectionType, Section> sections = resume.getSections();
			writer.writeInt(sections.size());
			sections.forEach((sectionType, section) -> {
				try {
					writer.writeUTF(sectionType.name());
					if (section instanceof TextSection) {
						writer.writeUTF(TEXT_SECTION);
						writer.writeUTF(((TextSection) section).getContent());
					} else if (section instanceof ListSection) {
						writer.writeUTF(LIST_SECTION);
						List<String> items = ((ListSection) section).getItems();
						writer.writeInt(items.size());
						items.forEach(item -> {
							try {
								writer.writeUTF(item);
							} catch (IOException e) {
								throw new StorageException("Error in method doWrite", e);
							}
						});
					} else if (section instanceof OrganizationSection) {
						writer.writeUTF(ORGANIZATION_SECTION);
						List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
						writer.writeInt(organizations.size());
						organizations.forEach(organization -> {
							Link homePage = organization.getHomePage();
							try {
								writer.writeUTF(homePage.getName());
								writer.writeUTF(homePage.getLink());

								List<Position> positions = organization.getPositions();
								writer.writeInt(positions.size());
								positions.forEach(position -> {
									try {
										writer.writeUTF(position.getStartDate().toString());
										writer.writeUTF(position.getEndDate().toString());
										writer.writeUTF(position.getTitle());

										List<String> descriptions = position.getDescriptions();
										writer.writeInt(descriptions.size());
										descriptions.forEach(description -> {
											try {
												writer.writeUTF(description);
											} catch (Exception e) {
												throw new StorageException("Error in method doWrite", e);
											}
										});
									} catch (IOException e) {
										throw new StorageException("Error in method doWrite", e);
									}
								});
							} catch (IOException e) {
								throw new StorageException("Error in method doWrite", e);
							}
						});
					} else {
						throw new StorageException("Doesn't processed case for " + section.getClass().getName());
					}
				} catch (IOException e) {
					throw new StorageException("Error in method doWrite", e);
				}
			});
			writer.flush();
		}
	}
}
