package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 *
 * @author Wayne.M
 * 2019年2月16日
 */
@Configuration
public class TaskExecutePool {

    private int corePoolSize = 200;

    private int maxPoolSize = 200;

    private int keepAliveSeconds = 60;

    private int queueCapacity = 200;

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskAsyncPool() {
        return buildThreadPool();
    }

    private ThreadPoolTaskExecutor buildThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //初始线程大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程大小
        executor.setMaxPoolSize(maxPoolSize);
        //设置队列长度
        executor.setQueueCapacity(queueCapacity);
        //设置多长时间，线程回收
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //当pool已经达到max size的时候，不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("taskExecutor-");
        executor.initialize();
        return executor;
    }
}
