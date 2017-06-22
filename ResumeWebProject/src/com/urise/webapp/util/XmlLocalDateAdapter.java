package com.urise.webapp.util;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlLocalDateAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public LocalDate unmarshal(String value) throws Exception {
		return LocalDate.parse(value, DateUtil.PATTERN_3);
	}

	@Override
	public String marshal(LocalDate value) throws Exception {
		return value.format(DateUtil.PATTERN_3);
	}
}
