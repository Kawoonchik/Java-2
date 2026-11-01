package com.java.lingvo.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class QuartzConfig {


    @Bean
    public JobDetail cleanupJobDetail() {
        return JobBuilder.newJob(CleanupJob.class)
                .withIdentity("cleanupJob", "maintenance") // Чітка назва і група
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cleanupJobTrigger(JobDetail cleanupJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(cleanupJobDetail)
                .withIdentity("cleanupTrigger", "maintenance")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Europe/Kyiv"))
                        .withMisfireHandlingInstructionDoNothing())
                .build();
    }


    @Bean
    public JobDetail reminderJobDetail() {
        return JobBuilder.newJob(ReminderJob.class)
                .withIdentity("reminderJob", "notifications")
                .storeDurably()
                .usingJobData("messageType", "DEFAULT_MESSAGE")
                .build();
    }


    @Bean
    public Trigger dailyReminderTrigger(JobDetail reminderJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(reminderJobDetail)
                .withIdentity("dailyReminderTrigger", "notifications")
                .usingJobData("messageType", "DAILY")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Europe/Kyiv"))
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }

    @Bean
    public Trigger errorSimulationTrigger(JobDetail reminderJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(reminderJobDetail)
                .withIdentity("errorSimulationTrigger", "notifications")

                .usingJobData("messageType", "ERROR_TEST")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Europe/Kyiv"))
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }
}