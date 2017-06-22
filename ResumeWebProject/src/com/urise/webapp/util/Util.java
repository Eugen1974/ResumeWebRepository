package com.urise.webapp.util;

import java.util.Collection;
import java.util.Objects;

public class Util {

	private Util() {
	}

	public static boolean isEmpty(String str) {
		return Objects.isNull(str) || str.trim().length() == 0;
	}

	public static boolean isArrayEmpty(Object[] array) {
		return Objects.isNull(array) || array.length == 0;
	}

	public static boolean isCollectionEmpty(Collection<?> collection) {
		return Objects.isNull(collection) || collection.size() == 0;
	}
}
