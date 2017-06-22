package com.urise.webapp.examples.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class MainCounter {

	public static void main(String[] args) throws InterruptedException {
		final int amountThreads = 10_000;
		final AtomicInteger counter = new AtomicInteger(0);
		final CountDownLatch latch = new CountDownLatch(amountThreads);

		Runnable run = () -> {
			try {
				Thread.sleep(100);
				for (int j = 0; j < 100; j++) {
					counter.incrementAndGet();
				}
				latch.countDown();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		};

		for (int i = 0; i < amountThreads; i++) {
			new Thread(run).start();
		}

		latch.await();
		System.out.println("counter=" + counter);
	}
}
