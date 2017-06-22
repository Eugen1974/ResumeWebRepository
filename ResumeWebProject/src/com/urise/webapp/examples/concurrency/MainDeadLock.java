package com.urise.webapp.examples.concurrency;

public class MainDeadLock {

	public static void main(String[] args) {
		String str1 = "str1";
		String str2 = "str2";
		f(str1, str2);
		f(str2, str1);
	}

	public static void f(Object ob1, Object ob2) {
		Runnable run = () -> {
			synchronized (ob1) {
				System.out.println("locking " + ob1 + " " + Thread.currentThread().getName());
				try {
					Thread.sleep(300);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				synchronized (ob2) {
					System.out.println("locking " + ob2 + " " + Thread.currentThread().getName());
				}
			}
		};
		new Thread(run).start();
	}
}
