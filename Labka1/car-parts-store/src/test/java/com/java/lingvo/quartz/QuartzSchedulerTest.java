package com.java.lingvo.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuartzConfigTest {

    private final QuartzConfig quartzConfig = new QuartzConfig();

    @Test
    void triggerDataMap_ShouldOverrideJobDetailDefaults() {
        // GIVEN:
        JobDetail jobDetail = quartzConfig.reminderJobDetail();
        Trigger trigger = quartzConfig.dailyReminderTrigger(jobDetail);

        // WHEN:
        JobDataMap mergedMap = new JobDataMap();
        mergedMap.putAll(jobDetail.getJobDataMap());
        mergedMap.putAll(trigger.getJobDataMap());

        // THEN:
        assertEquals("DAILY", mergedMap.getString("messageType"),
                "Параметр з тригера має перевизначати дефолтне значення з JobDetail");
    }

    @Test
    void cronTrigger_ShouldHaveExplicitTimeZone() {
        // GIVEN:
        CronTrigger trigger = (CronTrigger) quartzConfig.cleanupJobTrigger(quartzConfig.cleanupJobDetail());

        // THEN:
        assertEquals("Europe/Kyiv", trigger.getTimeZone().getID(),
                "Cron-тригер повинен мати явно визначений часовий пояс Europe/Kyiv");
    }

    @Test
    void reminderJob_ShouldThrowException_WhenMandatoryParameterIsMissing() {
        // GIVEN:
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());

        QuartzBusinessService mockService = mock(QuartzBusinessService.class);
        ReminderJob reminderJob = new ReminderJob(mockService);

        // WHEN & THEN:
        assertThrows(IllegalArgumentException.class, () -> reminderJob.execute(context),
                "Якщо відсутній обов'язковий ключ 'messageType', повинна виникати явна помилка");
    }

    @Test
    void reminderJob_ShouldHandleException_WhenServiceFails() {
        // GIVEN:
        QuartzBusinessService mockService = mock(QuartzBusinessService.class);
        doThrow(new RuntimeException("Збій сервісу")).when(mockService).sendReminder("ERROR_TEST");

        JobDataMap dataMap = new JobDataMap();
        dataMap.put("messageType", "ERROR_TEST");

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap()).thenReturn(dataMap);

        ReminderJob reminderJob = new ReminderJob(mockService);

        // WHEN & THEN:
        JobExecutionException exception = assertThrows(JobExecutionException.class, () -> reminderJob.execute(context));
        assertFalse(exception.refireImmediately(), "Завдання не повинно зациклюватися у нескінченному повторі");
    }
}