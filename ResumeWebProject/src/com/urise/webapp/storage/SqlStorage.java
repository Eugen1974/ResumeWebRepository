package com.urise.webapp.storage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
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
import com.urise.webapp.sql.ISqlExecutor;
import com.urise.webapp.sql.ISqlExecutorVoid;
import com.urise.webapp.sql.ISqlTransaction;
import com.urise.webapp.sql.SqlHelper;

public class SqlStorage implements IStorage {

	private static class MyBoolean {
		boolean b;
	}

	@Override
	public boolean update(Resume resume) {
		MyBoolean result = new MyBoolean();
		ISqlTransaction sqlTransaction = connection -> {
			try (PreparedStatement statement = connection
					.prepareStatement("UPDATE RESUME SET FULL_NAME=? WHERE UUID=?")) {
				statement.setString(1, resume.getFullName());
				statement.setString(2, resume.getUuid());
				result.b = statement.executeUpdate() == 1;
				if (!result.b) {
					throw new NotExistStorageException(resume.getUuid());
				}
			}
			deleteContacts(connection, resume.getUuid());
			insertContacts(connection, resume);

			deleteSections(connection, resume.getUuid());
			insertSections(connection, resume);
		};
		SqlHelper.executeTransaction(sqlTransaction, "Error in method update !");
		return result.b;
	}

	@Override
	public boolean save(Resume resume) {
		MyBoolean result = new MyBoolean();
		ISqlTransaction sqlTransaction = connection -> {
			try (PreparedStatement statement = connection
					.prepareStatement("INSERT INTO RESUME (UUID, FULL_NAME) VALUES (?,?)")) {
				statement.setString(1, resume.getUuid());
				statement.setString(2, resume.getFullName());
				result.b = statement.executeUpdate() == 1;
				if (!result.b) {
					throw new ExistStorageException(resume.getUuid());
				}
			}
			insertContacts(connection, resume);
			insertSections(connection, resume);
		};
		SqlHelper.executeTransaction(sqlTransaction, "Error in method save !");
		return result.b;
	}

	@Override
	public boolean delete(String uuid) {
		MyBoolean result = new MyBoolean();
		ISqlTransaction sqlTransaction = connection -> {
			deleteContacts(connection, uuid);
			deleteSections(connection, uuid);
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM RESUME WHERE UUID=?")) {
				statement.setString(1, uuid);
				result.b = statement.executeUpdate() == 1;
				if (!result.b) {
					throw new NotExistStorageException(uuid);
				}
			}
		};
		SqlHelper.executeTransaction(sqlTransaction, "Error in method delete !");
		return result.b;
	}

	@Override
	public Resume get(String uuid) {
		ISqlExecutor<Resume> sqlExecutor = statement -> {
			statement.setString(1, uuid);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return new Resume(uuid, resultSet.getString("FULL_NAME"), getContactsByUuid(uuid),
							getSectionsByUuid(uuid));
				}
				throw new NotExistStorageException(uuid);
			}
		};
		return SqlHelper.getResult("SELECT FULL_NAME FROM RESUME WHERE UUID=?", sqlExecutor, "Error in method get !");
	}

	@Override
	public void clear() {
		ISqlTransaction sqlTransaction = connection -> {
			try (Statement statement = connection.createStatement()) {
				statement.addBatch("DELETE FROM POSITION_IN_ORGANIZATION");
				statement.addBatch("DELETE FROM DESCRIPTION_POSITION");
				statement.addBatch("DELETE FROM ORGANIZATION_SECTION");
				statement.addBatch("DELETE FROM POSITION");
				statement.addBatch("DELETE FROM ORGANIZATION");
				statement.addBatch("DELETE FROM LINK");
				statement.addBatch("DELETE FROM LIST_SECTION");
				statement.addBatch("DELETE FROM TEXT_SECTION");
				statement.addBatch("DELETE FROM CONTACT");
				statement.addBatch("DELETE FROM RESUME");
				statement.executeBatch();
			}
		};
		SqlHelper.executeTransaction(sqlTransaction, "Error in method clear !");
	}

	@Override
	public int getSize() {
		ISqlExecutor<Integer> sqlExecutor = statement -> {
			try (ResultSet resultSet = statement.executeQuery()) {
				resultSet.next();
				return resultSet.getInt(1);
			}
		};
		return SqlHelper.getResult("SELECT COUNT(*) FROM RESUME", sqlExecutor, "Error in method getSize !").intValue();
	}

	@Override
	public List<Resume> getAllSorted() {
		ISqlExecutor<List<Resume>> sqlExecutor = statement -> {
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Resume> resumes = new ArrayList<>();
				while (resultSet.next()) {
					String uuid = resultSet.getString("UUID");
					resumes.add(new Resume(uuid, resultSet.getString("FULL_NAME"), getContactsByUuid(uuid),
							getSectionsByUuid(uuid)));
				}
				return resumes;
			}
		};
		return SqlHelper.getResult("SELECT UUID, FULL_NAME FROM RESUME ORDER BY 2", sqlExecutor,
				"Error in method getAllSorted !");
	}

	public Map<ContactType, String> getContactsByUuid(String uuid) {
		ISqlExecutor<Map<ContactType, String>> sqlExecutor = statement -> {
			statement.setString(1, uuid);
			try (ResultSet resultSet = statement.executeQuery()) {
				Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
				while (resultSet.next()) {
					contacts.put(ContactType.valueOf(resultSet.getString("TYPE")), resultSet.getString("VALUE"));
				}
				return contacts;
			}
		};
		return SqlHelper.getResult("SELECT TYPE, VALUE FROM CONTACT WHERE UUID=?", sqlExecutor,
				"Error in method getContactsByUuid !");
	}

	public Map<SectionType, Section> getSectionsByUuid(String uuid) {
		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

		ISqlExecutor<List<String>> sqlExecutor10 = statement -> {
			statement.setString(1, uuid);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<String> typesTextSection = new ArrayList<>();
				while (resultSet.next()) {
					typesTextSection.add(resultSet.getString("TYPE"));
				}
				return typesTextSection;
			}
		};
		List<String> typesTextSection = SqlHelper.getResult(
				"SELECT DISTINCT TYPE FROM TEXT_SECTION WHERE UUID=? ORDER BY 1", sqlExecutor10,
				"Error in method getSectionsByUuid for TEXT_SECTION by UUID !");

		for (String typeTextSection : typesTextSection) {
			ISqlExecutorVoid sqlExecutor1 = statement -> {
				statement.setString(1, uuid);
				statement.setString(2, typeTextSection);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						sections.put(SectionType.valueOf(typeTextSection),
								new TextSection(resultSet.getString("CONTENT")));
					}
				}
			};
			SqlHelper.executeSql("SELECT CONTENT FROM TEXT_SECTION WHERE UUID=? AND TYPE=?", sqlExecutor1,
					"Error in method getSectionsByUuid for TEXT_SECTION by UUID & TYPE !");
		}

		ISqlExecutor<List<String>> sqlExecutor2 = statement -> {
			statement.setString(1, uuid);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<String> typesSections = new ArrayList<>();
				while (resultSet.next()) {
					typesSections.add(resultSet.getString("TYPE"));
				}
				return typesSections;
			}
		};
		List<String> typesListSection = SqlHelper.getResult(
				"SELECT DISTINCT TYPE FROM LIST_SECTION WHERE UUID=? ORDER BY 1", sqlExecutor2,
				"Error in method getSectionsByUuid for LIST_SECTION by TYPE !");

		for (String typeListSection : typesListSection) {

			ISqlExecutor<List<String>> sqlExecutor3 = statement -> {
				statement.setString(1, uuid);
				statement.setString(2, typeListSection);
				try (ResultSet resultSet = statement.executeQuery()) {
					List<String> items = new ArrayList<>();
					while (resultSet.next()) {
						items.add(resultSet.getString("ITEM"));
					}
					return items;
				}
			};
			List<String> items = SqlHelper.getResult("SELECT ITEM FROM LIST_SECTION WHERE UUID=? AND TYPE=? ORDER BY 1",
					sqlExecutor3, "Error in method getSectionsByUuid for LIST_SECTION by UUID & TYPE !");

			sections.put(SectionType.valueOf(typeListSection), new ListSection(items));
		}
		// Forming OrganizationSection
		ISqlExecutor<List<String>> sqlExecutor4 = statement -> {
			statement.setString(1, uuid);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<String> typesOrganizationSection = new ArrayList<>();
				while (resultSet.next()) {
					typesOrganizationSection.add(resultSet.getString("TYPE"));
				}
				return typesOrganizationSection;
			}
		};
		List<String> typesOrganizationSection = SqlHelper.getResult(
				"SELECT DISTINCT TYPE FROM ORGANIZATION_SECTION WHERE UUID=? ORDER BY 1", sqlExecutor4,
				"Error in method getSectionsByUuid in ORGANIZATION_SECTION by UUID !");

		for (String typeOrganizationSection : typesOrganizationSection) {
			ISqlExecutor<List<Integer>> sqlExecutor5 = statement -> {
				statement.setString(1, uuid);
				statement.setString(2, typeOrganizationSection);
				try (ResultSet resultSet = statement.executeQuery()) {
					List<Integer> listIdOrganizations = new ArrayList<>();
					while (resultSet.next()) {
						listIdOrganizations.add(resultSet.getInt("ID_ORGANIZATION"));
					}
					return listIdOrganizations;
				}
			};
			List<Integer> idOrganizations = SqlHelper.getResult(
					"SELECT DISTINCT ID_ORGANIZATION FROM ORGANIZATION_SECTION WHERE UUID=? AND TYPE=? ORDER BY 1",
					sqlExecutor5, "Error in method getSectionsByUuid in ORGANIZATION_SECTION by UUID & TYPE !");

			List<Organization> organizations = new ArrayList<>();
			for (Integer idOrganization : idOrganizations) {
				ISqlExecutor<Link> sqlExecutor6 = statement -> {
					statement.setInt(1, idOrganization);
					try (ResultSet resultSet = statement.executeQuery()) {
						if (resultSet.next()) {
							return new Link(resultSet.getString("NAME"), resultSet.getString("LINK"));
						}
						throw new NotExistStorageException("Link for resume with uuid=" + uuid + " & idOrganization="
								+ idOrganization + " doesn't exist !", uuid);
					}
				};
				Link link = SqlHelper.getResult(
						"SELECT NAME, LINK FROM LINK WHERE ID_LINK=( "
								+ "SELECT ID_LINK FROM ORGANIZATION WHERE ID_ORGANIZATION=?)",
						sqlExecutor6, "Error in method getSectionsByUuid for LINK by ID_ORGANIZATION !");

				ISqlExecutor<List<Integer>> sqlExecutor7 = statement -> {
					statement.setInt(1, idOrganization);
					try (ResultSet resultSet = statement.executeQuery()) {
						List<Integer> idPositions = new ArrayList<>();
						while (resultSet.next()) {
							idPositions.add(resultSet.getInt("ID_POSITION"));
						}
						return idPositions;
					}
				};
				List<Integer> idPositions = SqlHelper.getResult(
						"SELECT DISTINCT ID_POSITION FROM POSITION_IN_ORGANIZATION WHERE ID_ORGANIZATION=? ORDER BY 1",
						sqlExecutor7,
						"Error in method getSectionsByUuid for POSITION_IN_ORGANIZATION by ID_ORGANIZATION");

				List<Position> positions = new ArrayList<>();
				for (Integer idPosition : idPositions) {

					ISqlExecutor<List<String>> sqlExecutor8 = statement -> {
						statement.setInt(1, idPosition);
						try (ResultSet resultSet = statement.executeQuery()) {
							List<String> descriptions = new ArrayList<>();
							while (resultSet.next()) {
								descriptions.add(resultSet.getString("DESCRIPTION"));
							}
							return descriptions;
						}
					};
					List<String> descriptions = SqlHelper.getResult(
							"SELECT DESCRIPTION FROM DESCRIPTION_POSITION WHERE ID_POSITION=? ORDER BY 1", sqlExecutor8,
							"Error in method getSectionsByUuid for DESCRIPTION_POSITION by ID_POSITION !");

					ISqlExecutor<Position> sqlExecutor9 = statement -> {
						statement.setInt(1, idPosition);
						try (ResultSet resultSet = statement.executeQuery()) {
							if (resultSet.next()) {
								return new Position(resultSet.getDate("START_DATE").toLocalDate(),
										resultSet.getDate("END_DATE").toLocalDate(), resultSet.getString("TITLE"),
										descriptions);
							}
							throw new NotExistStorageException(
									"Position with idPosition=" + idPosition + "& uuid=" + uuid + " doesn't exist !");
						}
					};
					Position position = SqlHelper.getResult(
							"SELECT START_DATE, END_DATE, TITLE FROM POSITION WHERE ID_POSITION=?", sqlExecutor9,
							"Error in method getSectionsByUuid for POSITION by ID_POSITION");
					positions.add(position);
				}
				organizations.add(new Organization(link, positions));
			}
			sections.put(SectionType.valueOf(typeOrganizationSection), new OrganizationSection(organizations));
		}
		return sections;
	}

	@Override
	public <SK> boolean isExist(SK uuid) {
		ISqlExecutor<Integer> sqlExecutor = statement -> {
			statement.setString(1, (String) uuid);
			try (ResultSet resultSet = statement.executeQuery()) {
				resultSet.next();
				return resultSet.getInt(1);
			}
		};
		Integer result = SqlHelper.getResult("SELECT COUNT(*) FROM RESUME WHERE UUID=?", sqlExecutor,
				"Error in method isExist !");
		return result > 0;
	}

	private void deleteContacts(Connection connection, String uuid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM CONTACT WHERE UUID=?")) {
			statement.setString(1, uuid);
			statement.executeUpdate();
		}
	}

	private void insertContacts(Connection connection, Resume resume) throws SQLException {
		try (PreparedStatement statement = connection
				.prepareStatement("INSERT INTO CONTACT (TYPE, VALUE, UUID) VALUES (?,?,?)")) {
			for (Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
				statement.setString(1, entry.getKey().name());
				statement.setString(2, entry.getValue());
				statement.setString(3, resume.getUuid());
				statement.addBatch();
			}
			statement.executeBatch();
		}
	}

	private void deleteSections(Connection connection, String uuid) throws SQLException {
		try (PreparedStatement statement1 = connection.prepareStatement("DELETE FROM TEXT_SECTION WHERE UUID=?");
				PreparedStatement statement2 = connection.prepareStatement("DELETE FROM LIST_SECTION WHERE UUID=?");
				PreparedStatement statement3 = connection
						.prepareStatement("DELETE FROM POSITION_IN_ORGANIZATION WHERE ID_ORGANIZATION IN "
								+ "(SELECT ID_ORGANIZATION FROM ORGANIZATION_SECTION WHERE UUID=?)");
				PreparedStatement statement4 = connection
						.prepareStatement("DELETE FROM DESCRIPTION_POSITION WHERE ID_POSITION IN "
								+ "(SELECT ID_POSITION FROM POSITION_IN_ORGANIZATION WHERE ID_ORGANIZATION IN "
								+ "(SELECT ID_ORGANIZATION FROM ORGANIZATION_SECTION WHERE UUID=?))");
				PreparedStatement statement5 = connection.prepareStatement("DELETE FROM POSITION WHERE ID_POSITION IN "
						+ "(SELECT ID_POSITION FROM POSITION_IN_ORGANIZATION WHERE ID_ORGANIZATION IN "
						+ "(SELECT ID_ORGANIZATION FROM ORGANIZATION_SECTION WHERE UUID=?))");
				PreparedStatement statement6 = connection
						.prepareStatement("DELETE FROM ORGANIZATION_SECTION WHERE UUID=?")) {
			PreparedStatement statement7 = connection.prepareStatement(
					"DELETE FROM ORGANIZATION WHERE ID_ORGANIZATION IN " + "(SELECT ID_ORGANIZATION FROM ORGANIZATION "
							+ " EXCEPT " + " SELECT ID_ORGANIZATION FROM ORGANIZATION_SECTION)");
			PreparedStatement statement8 = connection.prepareStatement("DELETE FROM LINK WHERE ID_LINK IN "
					+ "(SELECT ID_LINK FROM LINK EXCEPT SELECT ID_LINK FROM ORGANIZATION)");
			statement1.setString(1, uuid);
			statement2.setString(1, uuid);
			statement3.setString(1, uuid);
			statement4.setString(1, uuid);
			statement5.setString(1, uuid);
			statement6.setString(1, uuid);

			statement1.executeUpdate();
			statement2.executeUpdate();
			statement3.executeUpdate();
			statement4.executeUpdate();
			statement5.executeUpdate();
			statement6.executeUpdate();
			statement7.executeUpdate();
			statement8.executeUpdate();
		}
	}

	private void insertSections(Connection connection, Resume resume) throws SQLException {
		try (PreparedStatement statement1 = connection
				.prepareStatement("INSERT INTO TEXT_SECTION (UUID, TYPE, CONTENT) VALUES (?, ?, ?)");
				PreparedStatement statement2 = connection
						.prepareStatement("INSERT INTO LIST_SECTION(UUID, TYPE, ITEM) VALUES (?, ?, ?)");
				PreparedStatement statement3 = connection
						.prepareCall("INSERT INTO LINK (ID_LINK, NAME, LINK) VALUES (?, ?, ?)");
				PreparedStatement statement4 = connection
						.prepareCall("INSERT INTO ORGANIZATION (ID_ORGANIZATION, ID_LINK) VALUES (?, ?)");
				PreparedStatement statement5 = connection.prepareCall(
						"INSERT INTO POSITION (ID_POSITION, START_DATE, END_DATE, TITLE) VALUES (?, ?, ?, ?)");
				PreparedStatement statement6 = connection
						.prepareCall("INSERT INTO DESCRIPTION_POSITION (ID_POSITION, DESCRIPTION) VALUES (?, ?)");
				PreparedStatement statement7 = connection.prepareCall(
						"INSERT INTO POSITION_IN_ORGANIZATION (ID_ORGANIZATION, ID_POSITION) VALUES (?, ?)");
				PreparedStatement statement8 = connection.prepareCall(
						"INSERT INTO ORGANIZATION_SECTION (UUID, TYPE, ID_ORGANIZATION) VALUES (?, ?, ?)")) {
			for (Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
				if (entry.getValue() instanceof TextSection) {
					statement1.setString(1, resume.getUuid());
					statement1.setString(2, entry.getKey().name());
					statement1.setString(3, ((TextSection) entry.getValue()).getContent());
					statement1.addBatch();
				} else if (entry.getValue() instanceof ListSection) {
					for (String item : ((ListSection) entry.getValue()).getItems()) {
						statement2.setString(1, resume.getUuid());
						statement2.setString(2, entry.getKey().name());
						statement2.setString(3, item);
						statement2.addBatch();
					}
				} else if (entry.getValue() instanceof OrganizationSection) {
					for (Organization organization : ((OrganizationSection) entry.getValue()).getOrganizations()) {
						Link homePage = organization.getHomePage();
						int idLink = getSeqId(connection, "LINK_ID_LINK_SEQ");
						statement3.setInt(1, idLink);
						statement3.setString(2, homePage.getName());
						statement3.setString(3, homePage.getLink());
						statement3.addBatch();

						int idOrganization = getSeqId(connection, "ORGANIZATION_ID_ORGANIZATION_SEQ");
						statement4.setInt(1, idOrganization);
						statement4.setInt(2, idLink);
						statement4.addBatch();

						for (Position position : organization.getPositions()) {
							int idPosition = getSeqId(connection, "POSITION_ID_POSITION_SEQ");
							statement5.setInt(1, idPosition);
							statement5.setDate(2, Date.valueOf(position.getStartDate()));
							statement5.setDate(3, Date.valueOf(position.getEndDate()));
							statement5.setString(4, position.getTitle());
							statement5.addBatch();

							for (String description : position.getDescriptions()) {
								statement6.setInt(1, idPosition);
								statement6.setString(2, description);
								statement6.addBatch();
							}

							statement7.setInt(1, idOrganization);
							statement7.setInt(2, idPosition);
							statement7.addBatch();
						}

						statement8.setString(1, resume.getUuid());
						statement8.setString(2, entry.getKey().name());
						statement8.setInt(3, idOrganization);
						statement8.addBatch();
					}
				}
			}
			statement1.executeBatch();
			statement2.executeBatch();
			statement3.executeBatch();
			statement4.executeBatch();
			statement5.executeBatch();
			statement6.executeBatch();
			statement7.executeBatch();
			statement8.executeBatch();
		}
	}

	private int getSeqId(Connection connection, String nameSequence) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT NEXTVAL('" + nameSequence + "')")) {
			resultSet.next();
			return resultSet.getInt(1);
		}
	}
}
