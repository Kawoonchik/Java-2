package com.java.lingvo.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = AopDemoServiceTest.TestConfig.class)
@ActiveProfiles("test")
class AopDemoServiceTest {

    @Import({AopDemoService.class, SanitizeTextAspect.class})
    @EnableAspectJAutoProxy
    static class TestConfig {
    }

    @Autowired
    private AopDemoService aopDemoService;

    @Test
    void getDirtyString_ShouldBeSanitizedByAspect_WhenCalledThroughProxy() {
        // Arrange
        String expected = "hello world";

        // Act: 
        String actual = aopDemoService.getDirtyString();

        // Assert: Перевіряємо, що аспект успішно перехопив виклик і змінив текст
        assertEquals(expected, actual, "Аспект @SanitizeText мав очистити текст і перевести його в нижній регістр");
    }
}