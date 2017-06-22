package com.urise.webapp.examples.others;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.urise.webapp.model.Resume;

public class MainReflection {
	public static void main(String[] args) throws Exception {
		Resume resume = new Resume("Женя");
		Class<Resume> classResume = Resume.class;
		
		Field field = classResume.getDeclaredFields()[1];
		field.setAccessible(true); // java.lang.IllegalAccessException: can not access a member of class Resume modifiers "private final"
		System.out.println(field.getName() + "=" + field.get(resume));
		field.set(resume, "Eugen");

		Method method = classResume.getDeclaredMethod("toString", new Class[] {});
		System.out.println("\n" + method.invoke(resume, new Object[] {}));
		
//		 I can create object in this way
		Resume r = Resume.class.newInstance();
//		r.set...
		System.out.println("\n" + r);
	}
}
