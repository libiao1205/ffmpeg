package com.example.demo.util.watermark;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author libiao
 * @date 2023/4/20
 */
public class ThreadFactoryInfo implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public ThreadFactoryInfo(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(null, r, namePrefix + nextId.getAndIncrement(), 0);
        return thread;
    }
}
