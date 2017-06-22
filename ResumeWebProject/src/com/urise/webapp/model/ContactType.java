package com.urise.webapp.model;

public enum ContactType {
	
	HOME_ADDRESS("Проживание"),
	HOME_PAGE("Домашняя страница"),
	HOME_PHONE("Домашний телефон"),
	MOBILE("Мобильный телефон"),
	MAIL("Почта"),
	SKYPE("Skype"),
	HABRAHABR("Habrahabr"),
	STACKOVERFLOW("Профиль StackOverFlow"),
	GITHUB("Профиль GitHub"),
	LINKEDIN("Профиль LinkedIn");

	private final String title;

	private ContactType(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "[name=" + name() + ", title=" + title + ", ordinal=" + ordinal() + "]";
	}
}