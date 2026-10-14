package com.java.lingvo.configuration;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(2);

        executor.setThreadNamePrefix("lingvo-async-");

        // ОБРОБКА ПЕРЕПОВНЕННЯ: Замість помилки 500 (TaskRejectedException),
        // надлишкові задачі будуть виконуватися в потоці, який їх викликав (CallerRunsPolicy).
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    // Глобальний обробник помилок для методів типу "запустив і забув" (void)
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            System.err.println("=== АСИНХРОННА ПОМИЛКА ===");
            System.err.println("Метод: " + method.getName());
            System.err.println("Причина: " + ex.getMessage());
            System.err.println("==========================");
        };
    }
}