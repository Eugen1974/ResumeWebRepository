package com.urise.webapp.examples.others;

import java.util.List;
import java.util.Objects;

import com.urise.webapp.model.Link;
import com.urise.webapp.model.ListSection;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.OrganizationSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.storage.ITestData;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.util.DateUtil;

public class MainResume {

	public static void main(String[] args) {
		showResume(ITestData.getResume2());
		System.out.println("\n+++++++++++++++++++++");
		showResumes();
	}

	public static void showResume(Resume resume) {
		System.out.println("\t\t" + resume.getFullName() + "\n");
		resume.getContacts().forEach(
				(contactType, value) -> System.out.println(String.format("%21s: %s", contactType.getTitle(), value)));

		resume.getSections().forEach((sectionType, section) -> {
			System.out.println("\n\t" + sectionType.getTitle() + ":\n------------------------");
			if (section instanceof TextSection) {
				System.out.println(((TextSection) section).getContent());
			} else if (section instanceof ListSection) {
				((ListSection) section).getItems().forEach(System.out::println);
			} else if (section instanceof OrganizationSection) {
				List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
				organizations.forEach(organization -> {
					System.out.println(String.format("%22s  %s", organization.getHomePage().getName(),
							organization.getHomePage().getLink()));
					organization.getPositions().forEach(position -> {
						if (Objects.equals(sectionType, SectionType.EDUCATION)) {
							System.out.println(position.getStartDate().format(DateUtil.PATTERN_2) + "-"
									+ position.getEndDate().format(DateUtil.PATTERN_2) + " " + position.getTitle());
						} else {
							String strDate = Objects.equals(position.getEndDate(), DateUtil.NOW)
									? DateUtil.NOW_STRING + " "
									: position.getEndDate().format(DateUtil.PATTERN_1);
							System.out.println(position.getStartDate().format(DateUtil.PATTERN_1) + "-" + strDate + " "
									+ position.getTitle());
						}
						position.getDescriptions().forEach(description -> System.out.println("\t\t" + description));
					});
					System.out.println();
				});
			}
		});
	}

	public static void showResumes() {
		new SqlStorage().getAllSorted().forEach(resume -> {
			System.out.println(resume.getUuid() + " " + resume.getFullName());

			resume.getContacts().forEach((key, value) -> System.out.println(key.getTitle() + " " + value));

			resume.getSections().forEach((sectionType, section) -> {
				System.out.println(sectionType.getTitle());
				if (section instanceof TextSection) {
					System.out.println("\t" + ((TextSection) section).getContent());
				} else if (section instanceof ListSection) {
					((ListSection) section).getItems().forEach(item -> System.out.println("\t" + item));
				} else if (section instanceof OrganizationSection) {
					((OrganizationSection) section).getOrganizations().forEach(organization -> {
						Link link = organization.getHomePage();
						System.out.println("\t" + link.getName() + " " + link.getLink());
						organization.getPositions().forEach(position -> {
							System.out.println("\tс " + position.getStartDate().format(DateUtil.PATTERN_1) + " по "
									+ position.getEndDate().format(DateUtil.PATTERN_1) + " " + position.getTitle());
							position.getDescriptions().forEach(description -> System.out.println("\t\t" + description));
						});
					});
				}
				System.out.println("---------------");
			});
		});
	}
}