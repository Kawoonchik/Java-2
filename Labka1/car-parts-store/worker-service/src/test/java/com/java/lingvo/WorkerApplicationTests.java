package com.java.lingvo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class WorkerApplicationTests {

    @Autowired
    private SchedulerFactoryBean quartzScheduler;

    @Test
    void contextLoads() {
        assertNotNull(quartzScheduler, "Quartz Scheduler має бути ініціалізований");
        System.out.println("✅ [Тест Worker-Service] Quartz успішно запущено!");
    }
}