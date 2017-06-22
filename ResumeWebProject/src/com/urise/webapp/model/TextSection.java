package com.urise.webapp.model;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TextSection extends Section {

	private static final long serialVersionUID = 1L;

	public static final TextSection EMPTY;
	static {
		EMPTY = new TextSection();
	}

	@JsonProperty
	private final String content;

	public TextSection() {
		this(new String());
	}

	public TextSection(String content) {
		Objects.requireNonNull(content, "content is null !");
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextSection) {
			TextSection temp = (TextSection) obj;
			return Objects.equals(content, temp.content);
		}
		return false;
	}

	@Override
	public String toString() {
		return "TextSection [content=" + content + "]";
	}

	@Override
	public TextSection clone() {
		return new TextSection(content);
	}
}
