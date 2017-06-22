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
public class ListSection extends Section {

	private static final long serialVersionUID = 1L;

	public static final ListSection EMPTY;
	static {
		EMPTY = new ListSection(Collections.unmodifiableList(Arrays.asList(new String())));
	}

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	@JsonProperty
	private final List<String> items;

	public ListSection() {
		this(new ArrayList<>());
	}

	public ListSection(String... items) {
		this(new ArrayList<>(Arrays.asList(items)));
	}

	public ListSection(List<String> items) {
		Objects.requireNonNull(items, "items is null !");
		this.items = items;
	}

	public List<String> getItems() {
		return items;
	}

	@Override
	public int hashCode() {
		return Objects.hash(items);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListSection) {
			ListSection temp = (ListSection) obj;
			return Objects.equals(items, temp.items);
		}
		return false;
	}

	@Override
	public String toString() {
		return "ListSection [items=" + items + "]";
	}

	@Override
	public ListSection clone() {
		return new ListSection(new ArrayList<>(items));
	}
}