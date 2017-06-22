package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class DateUtil {
	public static final DateTimeFormatter PATTERN_1 = DateTimeFormatter.ofPattern("MM.yyyy");
	public static final DateTimeFormatter PATTERN_2 = DateTimeFormatter.ofPattern("yyyy");
	public static final DateTimeFormatter PATTERN_3 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	public static final DateTimeFormatter PATTERN_4 = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	public static final DateTimeFormatter PATTERN_5 = DateTimeFormatter.ofPattern("hh:MM:ss");

	public static final String NOW_STRING = "сейчас";
	public static final LocalDate NOW = LocalDate.of(3_000, Month.JANUARY, 1);

	private DateUtil() {
	}

	public static LocalDate of(int year, Month month) {
		if (year < 1) {
			throw new IllegalArgumentException("Wrong year's number ! year=" + year);
		}
		Objects.requireNonNull(month, "month is null !");
		return LocalDate.of(year, month, 1);
	}

	public static java.sql.Date getSqlDate(LocalDate date) {
		Objects.requireNonNull(date, "date is null !");
		return java.sql.Date.valueOf(date);
	}

	public static String getStringFromLocalDate(LocalDate date, DateTimeFormatter pattern) {
		if (Objects.isNull(date)) {
			return null;
		}
		Objects.requireNonNull(pattern, "pattern is null !");
		return Objects.equals(date, NOW) ? NOW_STRING : date.format(pattern);
	}

	public static LocalDate getLocalDateFromString(String str, DateTimeFormatter pattern)
			throws IllegalArgumentException, DateTimeParseException {
		if (Util.isEmpty(str)) {
			return NOW;
		}
		Objects.requireNonNull(pattern, "pattern is null !");
		str = str.trim();
		String strPattern = new String();
		try {
			if (str.equalsIgnoreCase(NOW_STRING)) {
				return NOW;
			} else if (Objects.equals(pattern, PATTERN_1)) {
				strPattern = "MM.yyyy";
				return YearMonth.parse(str, PATTERN_1).atDay(1);
			} else if (Objects.equals(pattern, PATTERN_2)) {
				strPattern = "yyyy";
				return Year.parse(str, PATTERN_2).atDay(1);
			} else {
				throw new IllegalArgumentException(
						"Wrong pattern of date. Must be MM.yyyy or yyyy ! pattern=" + pattern);
			}
		} catch (DateTimeParseException e) {
			throw new DateTimeParseException(
					"Wrong date " + str + " ! Format must be " + strPattern + " Error in position " + e.getErrorIndex(),
					str, e.getErrorIndex());
		}
	}
}
