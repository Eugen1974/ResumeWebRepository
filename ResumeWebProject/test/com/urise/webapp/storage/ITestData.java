package com.urise.webapp.storage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
import com.urise.webapp.util.Config;
import com.urise.webapp.util.DateUtil;

public interface ITestData {

	String DIRECTORY_OF_RESUMES = Config.getInstance().getStorageDir();

	String UUID_0 = "000";
	String UUID_1 = "111";
	String UUID_2 = "222";
	String UUID_3 = "333";

	String FULL_NAME_0 = "Резюме 0";
	String FULL_NAME_1 = "Резюме 1";
	String FULL_NAME_2 = "Евгений Эсаулов";
	String FULL_NAME_3 = "Eugen";

	Resume RESUME_0 = getResume0();
	Resume RESUME_1 = getResume1();
	Resume RESUME_2 = getResume2();
	Resume RESUME_3 = getResume3();

	File FILE_0 = new File(DIRECTORY_OF_RESUMES, UUID_0);
	File FILE_1 = new File(DIRECTORY_OF_RESUMES, UUID_1);
	File FILE_2 = new File(DIRECTORY_OF_RESUMES, UUID_2);
	File FILE_3 = new File(DIRECTORY_OF_RESUMES, UUID_3);

	static Resume getResume0() {
		Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
		contacts.put(ContactType.GITHUB, "http://github");
		contacts.put(ContactType.LINKEDIN, "http://linkedin");
		contacts.put(ContactType.MAIL, "resume0@yandex.ru");

		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
		sections.put(SectionType.ACHIEVEMENT, new ListSection("Достижение 1", "Достижение 2", "Достижение 3"));
		return new Resume(UUID_0, FULL_NAME_0, contacts, sections);
	}

	static Resume getResume1() {
		Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
		contacts.put(ContactType.HABRAHABR, "http://habrahabr");
		contacts.put(ContactType.HOME_ADDRESS, "http://home_address");
		contacts.put(ContactType.MAIL, "resume1@gmail.com");

		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
		sections.put(SectionType.QUALIFICATIONS, new ListSection("Квалификация 1", "Квалификация 2", "Квалификация 3"));
		return new Resume(UUID_1, FULL_NAME_1, contacts, sections);
	}

	static Resume getResume2() {
		Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
		contacts.put(ContactType.HOME_ADDRESS, "Украина, г.Кривой Рог");
		contacts.put(ContactType.HOME_PAGE, "http://eugen.ua");
		contacts.put(ContactType.HOME_PHONE, "8(0564)11-22-33");
		contacts.put(ContactType.MOBILE, "+38(098)444-55-66");
		contacts.put(ContactType.MAIL, "eugen_ua@mail.ru");
		contacts.put(ContactType.SKYPE, "https://skype.com");
		contacts.put(ContactType.HABRAHABR, "https://habrahabr.ru");
		contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com");
		contacts.put(ContactType.GITHUB, "https://github.com");
		contacts.put(ContactType.LINKEDIN, "https://www.linkedin.com");

		Section sectionPersonal = new TextSection("Аналитический склад ума. Пурист кода и архитектуры.");
		Section sectionObjective = new TextSection("Ведущий преподаватель. PL/SQL Developer.");
		Section sectionAchievement = new ListSection(Arrays.asList("Налаживание процесса.", "Разработка приложения.",
				"Разработка проектов.", "Реализация аутентификации.", "Реализация протоколов.", "Реализация с нуля.",
				"Создание JavaEE фреймворка."));
		Section sectionQualifications = new ListSection(Arrays.asList("DB: Oracle.", "Languages: PL/SQL, Java.",
				"Version control: Git.", "Инструменты: Developer Suite 10G, PL/SQL Developer v7.1, Eclipse.",
				"Родной русский, английский \"medium\"."));

		List<Organization> orgExperience = new ArrayList<>();
		orgExperience.add(new Organization(new Link("Java Online Courses", "http://javaops.ru"),
				Arrays.asList(new Position(LocalDate.of(2013, 10, 1), DateUtil.NOW, "Автор и преподаватель.",
						Arrays.asList("Java Enterprise.", "Веб-сервисы.", "Практика Java.")))));

		orgExperience.add(new Organization(new Link("Wrike", "https://wrike.com"),
				Arrays.asList(new Position(LocalDate.of(2014, 10, 1), LocalDate.of(2016, 1, 1),
						"Старший разработчик (backend).", Arrays.asList("Проектирование и разработка.")))));

		orgExperience.add(new Organization(new Link("RIT Center", "https://ritcenter.com"),
				Arrays.asList(new Position(LocalDate.of(2012, 4, 1), LocalDate.of(2014, 10, 1), "Java архитектор.",
						Arrays.asList("Организация процесса разработки.")))));

		orgExperience.add(new Organization(new Link("Luxoft (Deutsche Bank)", "https://luxoft.com"),
				Arrays.asList(new Position(LocalDate.of(2010, 12, 1), LocalDate.of(2012, 4, 1), "Ведущий программист.",
						Arrays.asList("Участие в проекте.")))));

		orgExperience.add(new Organization(new Link("Yota", "https://yota.com"),
				Arrays.asList(new Position(LocalDate.of(2008, 6, 1), LocalDate.of(2010, 12, 1), "Ведущий специалист.",
						Arrays.asList("Дизайн и имплементация.")))));

		orgExperience.add(new Organization(new Link("Enkata", "https://enkata.com"),
				Arrays.asList(new Position(LocalDate.of(2007, 3, 1), LocalDate.of(2008, 6, 1), "Разработчик ПО.",
						Arrays.asList("Реализация клиентской части.")))));

		orgExperience.add(new Organization(new Link("Siemens AG", "https://siemens.com"),
				Arrays.asList(new Position(LocalDate.of(2005, 1, 1), LocalDate.of(2007, 2, 1), "Разработчик ПО.",
						Arrays.asList("Разработка информационной модели.")))));

		orgExperience.add(new Organization(new Link("Alcatel", "https://alcatel.com"),
				Arrays.asList(new Position(LocalDate.of(1997, 9, 1), LocalDate.of(2005, 1, 1),
						"Инженер по тестированию.", Arrays.asList("Тестирование, отладка.")))));

		List<Organization> orgEducation = new ArrayList<>();
		orgEducation.add(new Organization(new Link("Киевский политех", "https://kpi.ua"),
				Arrays.asList(
						new Position(LocalDate.of(1993, 1, 1), LocalDate.of(1996, 1, 1), "Аспирантура.",
								Arrays.asList("Описание аспирантуры")),
						new Position(LocalDate.of(1987, 1, 1), LocalDate.of(1993, 1, 1), "Инженер.",
								Arrays.asList("Описание инженерной должности")))));

		orgEducation.add(new Organization(new Link("Заочная школа МФТИ", "http://www.school.mipt.ru"),
				Arrays.asList(new Position(LocalDate.of(1984, 1, 1), LocalDate.of(1987, 1, 1), "Учёба",
						Arrays.asList("закончил с отличием :)")))));

		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
		sections.put(SectionType.PERSONAL, sectionPersonal);
		sections.put(SectionType.OBJECTIVE, sectionObjective);
		sections.put(SectionType.ACHIEVEMENT, sectionAchievement);
		sections.put(SectionType.QUALIFICATIONS, sectionQualifications);
		sections.put(SectionType.EXPERIENCE, new OrganizationSection(orgExperience));
		sections.put(SectionType.EDUCATION, new OrganizationSection(orgEducation));

		return new Resume(UUID_2, FULL_NAME_2, contacts, sections);
	}

	static Resume getResume3() {
		Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
		contacts.put(ContactType.MAIL, "resume3@rambler.ru");
		contacts.put(ContactType.MOBILE, "http://mobile");

		Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
		Organization organization1 = new Organization(new Link("name3", "link3"),
				Arrays.asList(
						new Position(LocalDate.now().minusDays(700), LocalDate.now().minusDays(600), "Title5",
								Arrays.asList("description13", "description14", "description15")),
						new Position(LocalDate.now().minusDays(500), LocalDate.now().minusDays(400), "Title6",
								Arrays.asList("description16", "description17", "description18"))));
		Organization organization2 = new Organization(new Link("name4", "link4"),
				Arrays.asList(
						new Position(LocalDate.now().minusDays(300), LocalDate.now().minusDays(200), "Title7",
								Arrays.asList("description19", "description20", "description21")),
						new Position(LocalDate.now().minusDays(100), LocalDate.now(), "Title8",
								Arrays.asList("description22", "description23", "description24"))));

		sections.put(SectionType.EXPERIENCE, new OrganizationSection(organization1));
		sections.put(SectionType.ACHIEVEMENT, new ListSection("Достижение 1-е", "Достижение 2-е", "Достижение 3-е"));
		sections.put(SectionType.PERSONAL, new TextSection("Personal text"));
		sections.put(SectionType.EDUCATION, new OrganizationSection(organization2));
		return new Resume(UUID_3, FULL_NAME_3, contacts, sections);
	}
}