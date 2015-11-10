package com.darwinsys.soundrec;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadUtils {
	
	private final static Executor threadpool = Executors.newFixedThreadPool(1);
	
	public static void execute(final Runnable r) {
		threadpool.execute(r);
	}
	
	static void executeAndWait(final Runnable r) {
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted, eh? DOH!" + e);
		}
	}
}
