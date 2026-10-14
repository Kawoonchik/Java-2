package com.java.lingvo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AopAspectsUnitTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private RetryOperation retryOperation;

    @Mock
    private FeatureToggle featureToggle;

    //
    @Test
    void retryAspect_ShouldRetryAndSucceed_OnSecondAttempt() throws Throwable {
        // GIVEN
        RetryOperationAspect aspect = new RetryOperationAspect();
        when(retryOperation.attempts()).thenReturn(3);


        when(proceedingJoinPoint.proceed())
                .thenThrow(new RuntimeException("Помилка БД"))
                .thenReturn("Успіх");

        // WHEN
        Object result = aspect.retry(proceedingJoinPoint, retryOperation);

        // THEN
        assertEquals("Успіх", result);
        verify(proceedingJoinPoint, times(2)).proceed(); // Перевіряємо логіку advice (proceed викликано двічі)
    }

    @Test
    void retryAspect_ShouldThrowException_WhenAttemptsExhausted() throws Throwable {
        // GIVEN
        RetryOperationAspect aspect = new RetryOperationAspect();
        when(retryOperation.attempts()).thenReturn(2);
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Фатальна помилка"));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> aspect.retry(proceedingJoinPoint, retryOperation));
        verify(proceedingJoinPoint, times(2)).proceed(); // Ліміт - 2 виклики
    }

    //
    @Test
    void featureToggleAspect_ShouldThrowException_WhenFeatureIsDisabled() {
        // GIVEN
        FeatureToggleAspect aspect = new FeatureToggleAspect();
        when(featureToggle.value()).thenReturn("unknown-feature");

        // WHEN & THEN
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> aspect.checkFeature(featureToggle)
        );
        assertTrue(exception.getMessage().contains("unknown-feature"));
    }

    @Test
    void featureToggleAspect_ShouldDoNothing_WhenFeatureIsEnabled() {
        // GIVEN
        FeatureToggleAspect aspect = new FeatureToggleAspect();
        when(featureToggle.value()).thenReturn("new-ui"); // Ця фіча є в Set всередині аспекту

        // WHEN & THEN
        assertDoesNotThrow(() -> aspect.checkFeature(featureToggle));
    }

    //
    @Test
    void sanitizeTextAspect_ShouldModifyReturnedString() throws Throwable {
        // GIVEN
        SanitizeTextAspect aspect = new SanitizeTextAspect();
        when(proceedingJoinPoint.proceed()).thenReturn("   Брудний    Текст   ");

        // WHEN
        Object result = aspect.sanitize(proceedingJoinPoint);

        // THEN
        assertEquals("брудний текст", result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}