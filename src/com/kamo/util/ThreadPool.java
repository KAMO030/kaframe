package com.kamo.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    public static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,10,2, TimeUnit.MINUTES,new LinkedBlockingQueue<>(3));
}
