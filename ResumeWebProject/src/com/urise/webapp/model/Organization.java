package com.urise.webapp.model;

import java.io.Serializable;
import java.time.LocalDate;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.JacksonLocalDateDeserializer;
import com.urise.webapp.util.JacksonLocalDateSerializer;
import com.urise.webapp.util.XmlLocalDateAdapter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Organization EMPTY;
	static {
		EMPTY = new Organization(Link.EMPTY, Collections.unmodifiableList(Arrays.asList(Position.EMPTY)));
	}

	private final Link homePage;
	@XmlElementWrapper(name = "positions")
	@XmlElement(name = "position")
	@JsonProperty
	private final List<Position> positions;

	public Organization() {
		this(new Link(), new ArrayList<>());
	}

	public Organization(Link homePage, Position... positions) {
		this(homePage, new ArrayList<>(Arrays.asList(positions)));
	}

	public Organization(Link homePage, List<Position> positions) {
		Objects.requireNonNull(positions, "positions is null !");
		this.homePage = homePage;
		this.positions = positions;
	}

	public Link getHomePage() {
		return homePage;
	}

	public List<Position> getPositions() {
		return positions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(homePage, positions);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Organization) {
			Organization o = (Organization) obj;
			return Objects.equals(homePage, o.homePage) && Objects.equals(positions, o.positions);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Organization [homePage=" + homePage + ", positions=" + positions + "]";
	}

	@Override
	public Organization clone() {
		List<Position> list = new ArrayList<>();
		positions.forEach(position -> list.add(position.clone()));
		return new Organization(homePage.clone(), list);
	}

	@XmlType
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Position implements Serializable {

		private static final long serialVersionUID = 1L;

		public static final Position EMPTY;
		static {
			EMPTY = new Position();
			EMPTY.descriptions = Collections.unmodifiableList(Arrays.asList(new String()));
		}

		@XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
		@JsonDeserialize(using = JacksonLocalDateDeserializer.class)
		@JsonSerialize(using = JacksonLocalDateSerializer.class)
		private final LocalDate startDate;
		@XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
		@JsonDeserialize(using = JacksonLocalDateDeserializer.class)
		@JsonSerialize(using = JacksonLocalDateSerializer.class)
		private final LocalDate endDate;
		@JsonProperty
		private final String title;
		@XmlElementWrapper(name = "descriptions")
		@JsonProperty
		private List<String> descriptions;

		public Position() {
			startDate = null;
			endDate = null;
			title = new String();
			descriptions = new ArrayList<>();
		}

		public Position(LocalDate startDate, String title, List<String> descriptions) {
			this(startDate, DateUtil.NOW, title, descriptions);
		}

		public Position(LocalDate startDate, LocalDate endDate, String title, List<String> descriptions) {
			Objects.requireNonNull(startDate, "startDate is null !");
			Objects.requireNonNull(endDate, "endDate is null !");
			Objects.requireNonNull(title, "title is null !");
			Objects.requireNonNull(descriptions, "descriptions is null !");
			if (endDate.isBefore(startDate)) {
				throw new StorageException(new IllegalArgumentException("endDate before startDate ! endDate="
						+ endDate.format(DateUtil.PATTERN_3) + " startDate=" + startDate.format(DateUtil.PATTERN_3)));
			}
			this.startDate = startDate;
			this.endDate = endDate;
			this.title = title;
			this.descriptions = descriptions;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public String getTitle() {
			return title;
		}

		public void setDescriptions(List<String> descriptions) {
			checkSetDescription("descriptions");
			this.descriptions = descriptions;
		}

		public List<String> getDescriptions() {
			return descriptions;
		}

		public void addDescription(String description) {
			checkSetDescription("description");
			descriptions.add(description);
		}

		@Override
		public int hashCode() {
			return Objects.hash(startDate, endDate, title, descriptions);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Position) {
				Position temp = (Position) obj;
				return Objects.equals(startDate, temp.startDate) && Objects.equals(endDate, temp.endDate)
						&& Objects.equals(title, temp.title) && Objects.equals(descriptions, temp.descriptions);
			}
			return false;
		}

		@Override
		public String toString() {
			return "Position [startDate=" + startDate + ", endDate=" + endDate + ", title=" + title + ", descriptions="
					+ descriptions + "]";
		}

		@Override
		public Position clone() {
			LocalDate start = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
			LocalDate end = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
			return new Position(start, end, title, new ArrayList<>(descriptions));
		}

		private void checkSetDescription(String nameField) {
			if (Objects.equals(this, EMPTY)) {
				throw new UnsupportedOperationException(
						"Can't change field " + nameField + " in object Position.EMPTY !");
			}
		}
	}
}
