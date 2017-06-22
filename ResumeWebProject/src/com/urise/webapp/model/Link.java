package com.urise.webapp.model;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Link implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Link EMPTY;
	static {
		EMPTY = new Link();
	}

	@JsonProperty
	private final String name;
	@JsonProperty
	private final String link;

	public Link() {
		this(new String(), new String());
	}

	public Link(String name, String link) {
		Objects.requireNonNull(name, "name is null !");
		Objects.requireNonNull(link, "link is null !");
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, link);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Link) {
			Link temp = (Link) obj;
			return Objects.equals(name, temp.name) && Objects.equals(link, temp.link);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Link [name=" + name + ", link=" + link + "]";
	}

	@Override
	public Link clone() {
		return new Link(name, link);
	}
}