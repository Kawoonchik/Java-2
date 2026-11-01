package com.java.lingvo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SanitizeTextAspect {

    @Around("@annotation(com.java.lingvo.aop.SanitizeText)")
    public Object sanitize(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed(); // Виконуємо оригінальний метод

        // Змінюємо значення, що повертається
        if (result instanceof String) {
            String text = (String) result;
            return text.trim().replaceAll("\\s+", " ").toLowerCase();
        }
        return result;
    }
}