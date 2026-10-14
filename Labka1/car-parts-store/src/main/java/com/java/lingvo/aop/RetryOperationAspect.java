package com.java.lingvo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryOperationAspect {

    @Around("@annotation(retryOperation)")
    public Object retry(ProceedingJoinPoint pjp, RetryOperation retryOperation) throws Throwable {
        int attempts = retryOperation.attempts();
        Throwable lastException = null;

        for (int i = 1; i <= attempts; i++) {
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                lastException = e;
                System.out.println("Спроба " + i + " провалилася. " +
                        (i < attempts ? "Повторюємо..." : "Вичерпано ліміт."));
            }
        }
        throw lastException;
    }
}