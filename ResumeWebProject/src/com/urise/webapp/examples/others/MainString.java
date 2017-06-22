package com.urise.webapp.examples.others;

public class MainString {
	public static void main(String[] args) {
		// Strings are constant; their values cannot be changed after they are created.
		// StringBuilder A mutable sequence of characters

		String[] array = new String[] { "1", "2", "3", "4", "5" };
		StringBuilder result = new StringBuilder();
		for (String s : array) {
			result.append(s).append(", ");
		}
		System.out.println(result);
		
		String s1 = "abc";	String s3 = new String("abc");
		String s2 = "abc";	String s4 = new String("abc");

		System.out.println("\n" + (s1 == s2) + "\t" + (s3 == s4));
	}
}
