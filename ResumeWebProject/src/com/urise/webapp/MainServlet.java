package com.urise.webapp;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.Util;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ThreadLocal<SqlStorage> storage = ThreadLocal.withInitial(() -> new SqlStorage());

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String uuid = request.getParameter("uuid");
		String action = request.getParameter("action");
		if (Util.isEmpty(action) || action.equalsIgnoreCase(ActionWithResume.VIEW_ALL.name())) {
			request.setAttribute(NameOfAttribute.RESUMES.name(), storage.get().getAllSorted());
			request.getRequestDispatcher("/WEB-INF/jsp/listResumes.jsp").forward(request, response);
		} else if (action.equalsIgnoreCase(ActionWithResume.INSERT.name())
				|| action.equalsIgnoreCase(ActionWithResume.UPDATE.name())
				|| action.equalsIgnoreCase(ActionWithResume.VIEW.name())) {
			showResume(uuid, ActionWithResume.valueOf(action.toUpperCase()), request, response);
		} else if (action.equalsIgnoreCase(ActionWithResume.DELETE.name())) {
			try {
				storage.get().delete(uuid);
				response.sendRedirect("main?action=view_all");
			} catch (Exception e) {
				String errorText = e instanceof NotExistStorageException ? "There is no resume with uuid=" + uuid + " !"
						: e.toString();
				request.setAttribute(NameOfAttribute.ERROR.name(), errorText);
				request.getRequestDispatcher(CheckErrors.ERROR_JSP).forward(request, response);
			}
		} else if (action.equalsIgnoreCase(ActionWithResume.SAVE.name())) {
			try {
				String fullName = request.getParameter("fullName");
				if (CheckErrors.isErrorParameter(fullName, "fullName", request, response)) {
					return;
				}
				Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
				Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
				for (ContactType contactType : ContactType.values()) {
					String valueContact = request.getParameter(contactType.name());
					if (!Util.isEmpty(valueContact)) {
						contacts.put(contactType, valueContact.trim());
					}
				}
				for (SectionType sectionType : SectionType.values()) {
					if (Objects.equals(sectionType, SectionType.PERSONAL)
							|| Objects.equals(sectionType, SectionType.OBJECTIVE)) {
						String valueParameter = request.getParameter(sectionType.name());
						if (!Util.isEmpty(valueParameter)) {
							sections.put(sectionType, new TextSection(valueParameter.trim()));
						}
					} else if (Objects.equals(sectionType, SectionType.ACHIEVEMENT)
							|| Objects.equals(sectionType, SectionType.QUALIFICATIONS)) {
						String valueParameter = request.getParameter(sectionType.name());
						sections.put(sectionType, new ListSection(getListFromString(valueParameter)));
					} else if (Objects.equals(sectionType, SectionType.EXPERIENCE)
							|| Objects.equals(sectionType, SectionType.EDUCATION)) {
						long amountOrganizations = request.getParameterMap().keySet().stream()
								.filter(key -> key.contains(sectionType.name()) && key.contains("name")).count();
						List<Organization> organizations = new ArrayList<>();
						for (int i = 0; i < amountOrganizations; i++) {
							String prefix = sectionType.name() + i;
							String nameOrganization = request.getParameter(prefix + "name");
							String urlOrganization = request.getParameter(prefix + "link");
							String[] startDates = request.getParameterValues(prefix + "startDate");
							String[] endDates = request.getParameterValues(prefix + "endDate");
							String[] titles = request.getParameterValues(prefix + "title");
							String[] descriptions = request.getParameterValues(prefix + "description");
							DateTimeFormatter pattern = Objects.equals(sectionType, SectionType.EXPERIENCE)
									? DateUtil.PATTERN_1
									: DateUtil.PATTERN_2; // EDUCATION
							if (!Util.isEmpty(nameOrganization) && !Util.isArrayEmpty(startDates)
									&& !Util.isArrayEmpty(titles)) {
								Link link = new Link(nameOrganization.trim(),
										Util.isEmpty(urlOrganization) ? new String(" ") : urlOrganization.trim());
								List<Position> positions = new ArrayList<>();
								for (int j = 0; j < startDates.length; j++) {
									titles[j] = Util.isEmpty(titles[j]) ? new String(" ") : titles[j].trim();
									String[] _descriptions = new String[0];
									if (!Util.isArrayEmpty(descriptions) && !Util.isEmpty(descriptions[j])) {
										_descriptions = descriptions[j].split("\n");
										for (int k = 0; k < _descriptions.length; k++) {
											_descriptions[k] = _descriptions[k].trim();
										}
									}
									positions.add(new Position(DateUtil.getLocalDateFromString(startDates[j], pattern),
											DateUtil.getLocalDateFromString(endDates[j], pattern), titles[j],
											Arrays.asList(_descriptions)));
								}
								organizations.add(new Organization(link, positions));
							}
						}
						sections.put(sectionType, new OrganizationSection(organizations));
					} else {
						throw new IllegalArgumentException(
								"Doesn't processed case for sectionType=" + sectionType.name() + " !");
					}
				}
				if (storage.get().isExist(uuid)) {
					storage.get().update(new Resume(uuid, fullName, contacts, sections));
				} else {
					storage.get().save(new Resume(fullName, contacts, sections));
				}
				response.sendRedirect("main?action=view_all");
			} catch (Exception e) {
				request.setAttribute(NameOfAttribute.ERROR.name(), e.toString());
				request.getRequestDispatcher(CheckErrors.ERROR_JSP).forward(request, response);
			}
		} else {
			request.setAttribute(NameOfAttribute.ERROR.name(), "Wrong value for parameter action ! action=" + action);
			request.getRequestDispatcher(CheckErrors.ERROR_JSP).forward(request, response);
		}
	}

	private void showResume(String uuid, ActionWithResume action, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Resume resume;
			if (Objects.equals(action, ActionWithResume.INSERT)) {
				resume = Resume.EMPTY;
			} else { // UPDATE, VIEW
				resume = storage.get().get(uuid);
				if (Objects.equals(action, ActionWithResume.UPDATE)) {
					addEmptyOrganization(resume);
				}
			}
			String path = "/WEB-INF/jsp/";
			if (Objects.equals(action, ActionWithResume.INSERT) || Objects.equals(action, ActionWithResume.UPDATE)) {
				path += "updateResume.jsp";
			} else {
				path += "viewResume.jsp";
			}
			request.setAttribute(NameOfAttribute.RESUME.name(), resume);
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception e) {
			request.setAttribute(NameOfAttribute.ERROR.name(), e.toString());
			request.getRequestDispatcher(CheckErrors.ERROR_JSP).forward(request, response);
		}
	}

	private void addEmptyOrganization(Resume resume) {
		Objects.requireNonNull(resume, "resume is null !");
		for (SectionType sectionType : new SectionType[] { SectionType.EXPERIENCE, SectionType.EDUCATION }) {
			Section section = resume.getSection(sectionType);
			if (Objects.isNull(section)) {
				section = new OrganizationSection();
				resume.setSection(sectionType, section);
			}
			if (section instanceof OrganizationSection) {
				List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
				organizations.add(0, Organization.EMPTY);
				resume.setSection(sectionType, new OrganizationSection(organizations));
			} else {
				throw new ClassCastException("Wrong section's type ! Must be OrganizationSection. type="
						+ section.getClass().getName() + ", uuid=" + resume.getUuid());
			}
		}
	}

	private List<String> getListFromString(String str) {
		List<String> list = new ArrayList<>();
		if (!Util.isEmpty(str)) {
			String[] array = str.split("\r\n");
			if (!Util.isArrayEmpty(array)) {
				for (String s : array) {
					if (!Util.isEmpty(s)) {
						list.add(s.trim());
					}
				}
			}
		}
		return list;
	}
}
