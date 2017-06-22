package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationSection extends Section {

	private static final long serialVersionUID = 1L;

	public static final OrganizationSection EMPTY;
	static {
		EMPTY = new OrganizationSection(Collections.unmodifiableList(Arrays.asList(Organization.EMPTY)));
	}

	@XmlElementWrapper(name = "organizations")
	@XmlElement(name = "organization")
	@JsonProperty
	private final List<Organization> organizations;

	public OrganizationSection() {
		this(new ArrayList<>());
	}

	public OrganizationSection(Organization... organizations) {
		this(new ArrayList<>(Arrays.asList(organizations)));
	}

	public OrganizationSection(List<Organization> organizations) {
		Objects.requireNonNull(organizations, "organizations is null !");
		this.organizations = organizations;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(organizations);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrganizationSection) {
			OrganizationSection temp = (OrganizationSection) obj;
			return Objects.equals(organizations, temp.organizations);
		}
		return false;
	}

	@Override
	public String toString() {
		return "OrganizationSection [organizations=" + organizations + "]";
	}

	@Override
	public OrganizationSection clone() {
		List<Organization> list = new ArrayList<>();
		organizations.forEach(organization -> list.add(organization.clone()));
		return new OrganizationSection(list);
	}
}