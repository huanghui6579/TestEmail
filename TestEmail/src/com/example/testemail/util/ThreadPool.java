package com.example.testemail.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	public static ExecutorService getCachedPool() {
		return Executors.newCachedThreadPool();
	}
}
