package com.java.lingvo.quartz;

import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class ReminderJob implements Job {

    private final QuartzBusinessService businessService;

    public ReminderJob(QuartzBusinessService businessService) {
        this.businessService = businessService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getMergedJobDataMap();


        if (!dataMap.containsKey("messageType")) {
            throw new IllegalArgumentException("Параметр 'messageType' є обов'язковим для ReminderJob!");
        }

        String messageType = dataMap.getString("messageType");

        try {
            businessService.sendReminder(messageType);
        } catch (Exception e) {
            //
            JobExecutionException qe = new JobExecutionException(e);
            qe.setRefireImmediately(false);
            throw qe;
        }
    }
}